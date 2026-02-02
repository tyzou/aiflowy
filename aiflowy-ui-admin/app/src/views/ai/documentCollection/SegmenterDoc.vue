<script setup lang="ts">
import { reactive, ref } from 'vue';

import { $t } from '@aiflowy/locales';

import {
  ElForm,
  ElFormItem,
  ElInput,
  ElOption,
  ElSelect,
  ElSlider,
} from 'element-plus';

const formRef = ref();
const form = reactive({
  fileType: 'doc',
  splitterName: 'SimpleDocumentSplitter',
  chunkSize: 512,
  overlapSize: 128,
  regex: '',
  rowsPerChunk: 0,
  mdSplitterLevel: 1,
});
const fileTypes = [
  {
    label: $t('documentCollection.splitterDoc.document'),
    value: 'doc',
  },
];
const splitterNames = [
  {
    label: $t('documentCollection.splitterDoc.simpleDocumentSplitter'),
    value: 'SimpleDocumentSplitter',
  },
  {
    label: $t('documentCollection.splitterDoc.simpleTokenizeSplitter'),
    value: 'SimpleTokenizeSplitter',
  },
  {
    label: $t('documentCollection.splitterDoc.regexDocumentSplitter'),
    value: 'RegexDocumentSplitter',
  },
  {
    label: $t('documentCollection.splitterDoc.markdownHeaderSplitter'),
    value: 'MarkdownHeaderSplitter',
  },
];
const mdSplitterLevel = [
  {
    label: '#',
    value: 1,
  },
  {
    label: '##',
    value: 2,
  },
  {
    label: '###',
    value: 3,
  },
  {
    label: '####',
    value: 4,
  },
  {
    label: '#####',
    value: 5,
  },
  {
    label: '######',
    value: 6,
  },
];
const rules = {
  name: [
    { required: true, message: 'Please input Activity name', trigger: 'blur' },
  ],
  region: [
    {
      required: true,
      message: 'Please select Activity zone',
      trigger: 'change',
    },
  ],
};
defineExpose({
  getSplitterFormValues() {
    return form;
  },
});
</script>

<template>
  <div class="splitter-doc-container">
    <ElForm
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="auto"
      class="custom-form"
    >
      <ElFormItem
        :label="$t('documentCollection.splitterDoc.fileType')"
        prop="fileType"
      >
        <ElSelect v-model="form.fileType">
          <ElOption
            v-for="item in fileTypes"
            :key="item.value"
            v-bind="item"
            :label="item.label"
          />
        </ElSelect>
      </ElFormItem>
      <ElFormItem
        :label="$t('documentCollection.splitterDoc.splitterName')"
        prop="splitterName"
      >
        <ElSelect v-model="form.splitterName">
          <ElOption
            v-for="item in splitterNames"
            :key="item.value"
            v-bind="item"
            :label="item.label"
          />
        </ElSelect>
      </ElFormItem>
      <ElFormItem
        :label="$t('documentCollection.splitterDoc.chunkSize')"
        v-if="
          form.splitterName === 'SimpleDocumentSplitter' ||
          form.splitterName === 'SimpleTokenizeSplitter'
        "
        prop="chunkSize"
      >
        <ElSlider v-model="form.chunkSize" show-input :max="2048" />
      </ElFormItem>
      <ElFormItem
        :label="$t('documentCollection.splitterDoc.overlapSize')"
        v-if="
          form.splitterName === 'SimpleDocumentSplitter' ||
          form.splitterName === 'SimpleTokenizeSplitter'
        "
        prop="overlapSize"
      >
        <ElSlider v-model="form.overlapSize" show-input :max="2048" />
      </ElFormItem>
      <ElFormItem
        :label="$t('documentCollection.splitterDoc.regex')"
        prop="regex"
        v-if="form.splitterName === 'RegexDocumentSplitter'"
      >
        <ElInput v-model="form.regex" />
      </ElFormItem>
      <ElFormItem
        v-if="form.splitterName === 'MarkdownHeaderSplitter'"
        :label="$t('documentCollection.splitterDoc.mdSplitterLevel')"
        prop="splitterName"
      >
        <ElSelect v-model="form.mdSplitterLevel">
          <ElOption
            v-for="item in mdSplitterLevel"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </ElSelect>
      </ElFormItem>
    </ElForm>
  </div>
</template>

<style scoped>
.splitter-doc-container {
  height: 100%;
  width: 100%;
  align-items: center;
  display: flex;
  justify-content: center;
}
.custom-form {
  width: 500px;
}
.custom-form :deep(.el-input),
.custom-form :deep(.ElSelect) {
  width: 100%;
}
</style>
