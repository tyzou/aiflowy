<script setup lang="ts">
import type { ServerSentEventMessage } from 'fetch-event-stream';

import { computed, ref, watch } from 'vue';

export interface WorkflowStepsProps {
  workflowId: any;
  executeMessage: null | ServerSentEventMessage;
}
const props = defineProps<WorkflowStepsProps>();
const messages = ref<Array<any>>([]);
watch(
  () => props.executeMessage,
  (newMessage) => {
    if (newMessage && newMessage.data) {
      try {
        const parsedMessage = JSON.parse(newMessage.data);
        messages.value.push(parsedMessage);
      } catch (error) {
        console.error('解析消息失败:', error);
      }
    }
  },
  { immediate: true },
);
const message = computed(() => {
  return JSON.parse(props.executeMessage?.data || '{}');
});
</script>

<template>
  <div>
    <div>当前消息：{{ JSON.stringify(message) }}</div>
    <div>
      所有消息：
      <div v-for="(item, index) in messages" :key="index">
        {{ index + 1 }} ---> {{ item }}
      </div>
    </div>
  </div>
</template>

<style scoped></style>
