<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';

import { api } from '#/api/request';
import CategoryPanel from '#/components/categoryPanel/CategoryPanel.vue';
import PreviewSearchKnowledge from '#/views/ai/knowledge/PreviewSearchKnowledge.vue';

interface docPreviewType {
  filePath: string;
  fileName: string;
  content: Array<string>;
  sorting: number;
}
export interface FileInfo {
  filePath: string;
  fileName: string;
}
const props = defineProps({
  pageNumber: {
    default: 1,
    type: Number,
  },
  pageSize: {
    default: 10,
    type: Number,
  },
  knowledgeId: {
    default: '',
    type: String,
  },
  fliesList: {
    default: () => [],
    type: Array<FileInfo>,
  },
  splitterParams: {
    default: () => {},
    type: Object,
  },
});
const emit = defineEmits(['updateTotal']);
const documentList = ref<any[]>([]);
const route = useRoute();
defineExpose({
  getFilesData() {
    return documentList.value.length;
  },
});
const knowledgeIdRef = ref<string>((route.query.id as string) || '');
watch(
  () => props.pageNumber,
  (newVal) => {
    splitterDocPreview(
      newVal,
      props.pageSize,
      props.fliesList[0]!.filePath,
      'textSplit',
      props.fliesList[0]!.fileName,
    );
  },
);
watch(
  () => props.pageSize,
  (newVal) => {
    splitterDocPreview(
      props.pageNumber,
      newVal,
      props.fliesList[0]!.filePath,
      'textSplit',
      props.fliesList[0]!.fileName,
    );
  },
);
function splitterDocPreview(
  pageNumber: number,
  pageSize: number,
  filePath: string,
  operation: string,
  fileOriginName: string,
) {
  api
    .post(
      '/api/v1/aiDocument/textSplit',
      {},
      {
        params: {
          pageNumber,
          pageSize,
          filePath,
          operation,
          knowledgeId: knowledgeIdRef.value,
          fileOriginName,
          ...props.splitterParams,
        },
      },
    )
    .then((res) => {
      if (res.errorCode === 0) {
        documentList.value = res.data.previewData;
        emit('updateTotal', res.data.total);
      }
    });
}
onMounted(() => {
  if (props.fliesList.length === 0) {
    return;
  }
  splitterDocPreview(
    props.pageNumber,
    props.pageSize,
    props.fliesList[0]!.filePath,
    'textSplit',
    props.fliesList[0]!.fileName,
  );
});
</script>

<template>
  <div class="splitter-doc-container">
    <CategoryPanel
      :categories="fliesList"
      title-key="fileName"
      :need-hide-collapse="true"
      :expand-width="200"
      value-key="filePath"
      :default-selected-category="fliesList[0]!.filePath"
    />
    <PreviewSearchKnowledge :data="documentList" :hide-score="true" />
  </div>
</template>

<style scoped>
.splitter-doc-container {
  display: flex;
}
</style>
