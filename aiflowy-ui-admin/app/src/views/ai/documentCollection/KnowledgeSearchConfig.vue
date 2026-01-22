<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';

import { $t } from '@aiflowy/locales';

import { InfoFilled } from '@element-plus/icons-vue';
import {
  ElButton,
  ElForm,
  ElFormItem,
  ElInputNumber,
  ElMessage,
  ElOption,
  ElSelect,
  ElSwitch,
  ElTooltip,
} from 'element-plus';

import { api } from '#/api/request';

const props = defineProps({
  documentCollectionId: {
    type: String,
    required: true,
  },
});

onMounted(() => {
  getDocumentCollectionConfig();
});
const searchEngineEnable = ref(false);
const getDocumentCollectionConfig = () => {
  api
    .get(`/api/v1/documentCollection/detail?id=${props.documentCollectionId}`)
    .then((res) => {
      const { data } = res;
      searchConfig.docRecallMaxNum = data.options.docRecallMaxNum
        ? Number(data.options.docRecallMaxNum)
        : 5;
      searchConfig.simThreshold = data.options.simThreshold
        ? Number(data.options.simThreshold)
        : 0.5;
      searchConfig.searchEngineType = data.options.searchEngineType || 'lucene';
      searchEngineEnable.value = !!data.searchEngineEnable;
    });
};

const searchConfig = reactive({
  docRecallMaxNum: 5,
  simThreshold: 0.5,
  searchEngineType: 'lucene',
});

const submitConfig = () => {
  const submitData = {
    id: props.documentCollectionId,
    options: {
      docRecallMaxNum: searchConfig.docRecallMaxNum,
      simThreshold: searchConfig.simThreshold,
      searchEngineType: searchConfig.searchEngineType,
    },
    searchEngineEnable: searchEngineEnable.value,
  };

  api
    .post('/api/v1/documentCollection/update', submitData)
    .then(() => {
      ElMessage.success($t('documentCollectionSearch.message.saveSuccess'));
    })
    .catch((error) => {
      ElMessage.error($t('documentCollectionSearch.message.saveFailed'));
      console.error('保存配置失败：', error);
    });
};

const searchEngineOptions = [
  {
    label: 'Lucene',
    value: 'lucene',
  },
  {
    label: 'ElasticSearch',
    value: 'elasticSearch',
  },
];
const handleSearchEngineEnableChange = () => {
  api.post('/api/v1/documentCollection/update', {
    id: props.documentCollectionId,
    searchEngineEnable: searchEngineEnable.value,
  });
};
</script>

<template>
  <div class="search-config-sidebar">
    <div class="config-header">
      <h3>{{ $t('documentCollectionSearch.title') }}</h3>
    </div>

    <ElForm
      class="config-form"
      :model="searchConfig"
      label-width="100%"
      size="small"
    >
      <ElFormItem prop="docRecallMaxNum" class="form-item">
        <div class="form-item-label">
          <span>{{
            $t('documentCollectionSearch.docRecallMaxNum.label')
          }}</span>
          <ElTooltip
            :content="$t('documentCollectionSearch.docRecallMaxNum.tooltip')"
            placement="top"
            effect="dark"
            class="label-tooltip"
          >
            <InfoFilled class="info-icon" />
          </ElTooltip>
        </div>
        <div class="form-item-content">
          <ElInputNumber
            v-model="searchConfig.docRecallMaxNum"
            :min="1"
            :max="50"
            :step="1"
            :placeholder="$t('documentCollectionSearch.placeholder.count')"
            class="form-control"
          >
            <template #append>
              {{ $t('documentCollectionSearch.unit.count') }}
            </template>
          </ElInputNumber>
        </div>
      </ElFormItem>

      <ElFormItem prop="simThreshold" class="form-item">
        <div class="form-item-label">
          <span>{{ $t('documentCollectionSearch.simThreshold.label') }}</span>
          <ElTooltip
            :content="$t('documentCollectionSearch.simThreshold.tooltip')"
            placement="top"
            effect="dark"
            class="label-tooltip"
          >
            <InfoFilled class="info-icon" />
          </ElTooltip>
        </div>
        <div class="form-item-content">
          <ElInputNumber
            v-model="searchConfig.simThreshold"
            :min="0"
            :max="1"
            :step="0.01"
            show-input
            class="form-control"
          />
        </div>
      </ElFormItem>

      <!-- 搜索引擎启用开关 -->
      <ElFormItem class="form-item">
        <div class="form-item-label">
          <span>{{
            $t('documentCollectionSearch.searchEngineEnable.label')
          }}</span>
          <ElTooltip
            :content="$t('documentCollectionSearch.searchEngineEnable.tooltip')"
            placement="top"
            effect="dark"
            class="label-tooltip"
          >
            <InfoFilled class="info-icon" />
          </ElTooltip>
        </div>
        <div class="form-item-content">
          <ElSwitch
            v-model="searchEngineEnable"
            @change="handleSearchEngineEnableChange"
            :active-text="$t('documentCollectionSearch.switch.on')"
            :inactive-text="$t('documentCollectionSearch.switch.off')"
            class="form-control switch-control"
          />
        </div>
      </ElFormItem>

      <!-- 通过 searchEngineEnable 控制显示/隐藏 -->
      <ElFormItem
        v-if="searchEngineEnable"
        prop="searchEngineType"
        class="form-item"
      >
        <div class="form-item-label">
          <span>{{
            $t('documentCollectionSearch.searchEngineType.label')
          }}</span>
          <ElTooltip
            :content="$t('documentCollectionSearch.searchEngineType.tooltip')"
            placement="top"
            effect="dark"
            class="label-tooltip"
          >
            <InfoFilled class="info-icon" />
          </ElTooltip>
        </div>
        <div class="form-item-content">
          <ElSelect
            v-model="searchConfig.searchEngineType"
            :placeholder="
              $t('documentCollectionSearch.searchEngineType.placeholder')
            "
            class="form-control"
          >
            <ElOption
              v-for="option in searchEngineOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </ElSelect>
        </div>
      </ElFormItem>
    </ElForm>

    <div class="config-footer">
      <ElButton type="primary" @click="submitConfig" class="submit-btn">
        {{ $t('documentCollectionSearch.button.save') }}
      </ElButton>
    </div>
  </div>
</template>

<style scoped>
.search-config-sidebar {
  width: 60%;
  height: 100%;
  padding: 16px;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}

.config-header {
  margin-bottom: 16px;
  border-bottom: 1px solid #e6e6e6;
  padding-bottom: 8px;
}

.config-header h3 {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
}

.config-form {
  margin-bottom: 24px;
}

.form-item {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-item-label {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
}

.label-tooltip {
  display: inline-block;
  cursor: help;
}

:deep(.form-item .el-form-item__content) {
  width: 100%;
  margin-left: 0 !important;
}

.form-item-content {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  margin-top: 4px;
}

.form-control {
  flex: 1;
  width: 100%;
  min-width: 0;
}

.switch-control {
  width: auto;
  flex: none;
  min-width: 80px;
}

.info-icon {
  font-size: 14px;
  color: #909399;
  cursor: help;
  width: 16px;
  height: 16px;
  flex-shrink: 0;
  flex: none;
}

.info-icon:hover {
  color: #409eff;
}

.submit-btn {
  width: 100%;
  padding: 8px 0;
}

.config-footer {
  position: sticky;
  bottom: 0;
  padding-top: 8px;
}

:deep(.el-form-item__content) {
  width: 100%;
  box-sizing: border-box;
}

:deep(.el-slider) {
  --el-slider-input-width: 60px;
}

:deep(.el-input-number),
:deep(.el-select) {
  width: 100%;
}
</style>
