<script setup lang="ts">
import { onMounted, ref } from 'vue';

import { ElButton, ElForm, ElFormItem, ElInput, ElMessage } from 'element-plus';

import { api } from '#/api/request';
import { $t } from '#/locales';

const oauthConfig = ref({
  wx_web: {
    appId: '',
    appSecret: '',
    redirectUri: 'https://your-domain/thirdAuth/callback/wx_web',
  },
  ding_talk: {
    appId: '',
    appSecret: '',
    redirectUri: 'https://your-domain/thirdAuth/callback/ding_talk',
  },
});
const saveLoading = ref(false);
type OAuthConfigKey = 'ding_talk' | 'wx_web';
onMounted(() => {
  getOptionByKey('wx_web');
  getOptionByKey('ding_talk');
});
function handleSave(key: OAuthConfigKey) {
  saveLoading.value = true;
  api
    .post('/api/v1/sysOption/saveOption', {
      key,
      value: JSON.stringify(oauthConfig.value[key]),
    })
    .then((res) => {
      saveLoading.value = false;
      if (res.errorCode === 0) {
        ElMessage.success($t('message.saveOkMessage'));
        getOptionByKey(key);
      }
    });
}
function getOptionByKey(key: OAuthConfigKey) {
  api
    .get('/api/v1/sysOption/getByKey', {
      params: {
        key,
      },
    })
    .then((res) => {
      if (res.errorCode === 0 && res.data) {
        oauthConfig.value[key] = JSON.parse(res.data.value);
      }
    });
}
</script>

<template>
  <div>
    <div class="settings-container">
      <div class="settings-config-container border-border border">
        <div class="mb-6">{{ $t('sysOption.oauthWxWeb') }}</div>
        <ElForm
          :model="oauthConfig.wx_web"
          class="demo-form-inline"
          label-width="150px"
        >
          <ElFormItem label="AppID">
            <ElInput v-model="oauthConfig.wx_web.appId" clearable />
          </ElFormItem>
          <ElFormItem label="AppSecret">
            <ElInput v-model="oauthConfig.wx_web.appSecret" clearable />
          </ElFormItem>
          <ElFormItem label="RedirectUrl">
            <ElInput v-model="oauthConfig.wx_web.redirectUri" clearable />
          </ElFormItem>
        </ElForm>
        <div class="settings-button-container">
          <ElButton
            :disabled="saveLoading"
            type="primary"
            @click="handleSave('wx_web')"
          >
            {{ $t('button.save') }}
          </ElButton>
        </div>
      </div>
    </div>
    <div class="settings-container">
      <div class="settings-config-container border-border border">
        <div class="mb-6">{{ $t('sysOption.oauthDingTalk') }}</div>
        <ElForm
          :model="oauthConfig.ding_talk"
          class="demo-form-inline"
          label-width="150px"
        >
          <ElFormItem label="ClientID">
            <ElInput v-model="oauthConfig.ding_talk.appId" clearable />
          </ElFormItem>
          <ElFormItem label="ClientSecret">
            <ElInput v-model="oauthConfig.ding_talk.appSecret" clearable />
          </ElFormItem>
          <ElFormItem label="RedirectUrl">
            <ElInput v-model="oauthConfig.ding_talk.redirectUri" clearable />
          </ElFormItem>
        </ElForm>
        <div class="settings-button-container">
          <ElButton
            :disabled="saveLoading"
            type="primary"
            @click="handleSave('ding_talk')"
          >
            {{ $t('button.save') }}
          </ElButton>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.settings-container {
  display: flex;
  flex-direction: column;
  padding: 0 143px;
  height: 100%;
  margin-top: 16px;
  margin-bottom: 16px;
}

.settings-config-container {
  background-color: var(--el-bg-color);
  width: 100%;
  padding: 20px;
  border-radius: 10px;
}
:deep(.el-form-item) {
  margin-bottom: 25px;
}
.settings-notice {
  margin-bottom: 20px;
  color: var(--el-color-danger);
}
.settings-button-container {
  display: flex;
  justify-content: flex-end;
}
</style>
