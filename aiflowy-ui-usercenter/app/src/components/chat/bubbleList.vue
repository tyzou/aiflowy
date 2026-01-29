<script setup lang="ts">
import { useUserStore } from '@aiflowy/stores';

import { ElAvatar } from 'element-plus';

import defaultAssistantAvatar from '#/assets/defaultAssistantAvatar.svg';
import defaultUserAvatar from '#/assets/defaultUserAvatar.png';

interface Props {
  bot: any;
  messages: any[];
}
const props = defineProps<Props>();
const store = useUserStore();

function getAssistantAvatar() {
  return props.bot.icon || defaultAssistantAvatar;
}
function getUserAvatar() {
  return store.userInfo?.avatar || defaultUserAvatar;
}
</script>

<template>
  <el-bubble-list :list="messages" max-height="calc(100vh - 345px)">
    <!-- 自定义头像 -->
    <template #avatar="{ item }">
      <ElAvatar
        :src="
          item.role === 'assistant' ? getAssistantAvatar() : getUserAvatar()
        "
        :size="40"
      />
    </template>

    <!-- 自定义头部 -->
    <template #header="{ item }">
      <div class="flex flex-col">
        <span class="text-foreground/50 text-xs">
          {{ item.created }}
        </span>
        <ElThinking
          v-if="item.reasoning_content"
          v-model="item.thinlCollapse"
          :content="item.reasoning_content"
          :status="item.thinkingStatus"
          class="mb-3"
        />
      </div>
    </template>

    <!-- 自定义气泡内容 -->
    <template #content="{ item }">
      <el-x-markdown :markdown="item.content" />
    </template>

    <!-- 自定义底部 -->
    <!--<template #footer="{ item }">
      <div class="flex items-center">
        <template v-if="item.role === 'assistant'">
          <ElButton :icon="RefreshRight" link />
          <ElButton :icon="CopyDocument" link />
        </template>
        <template v-else>
          <ElButton :icon="CopyDocument" link />
          <ElButton :icon="EditPen" link />
        </template>
      </div>
    </template>-->
  </el-bubble-list>
</template>

<style lang="css" scoped>
:deep(.el-bubble) {
  --bubble-content-max-width: calc(
    100% -
      calc(
        var(--el-bubble-avatar-placeholder-gap) + var(--el-avatar-size, 40px)
      )
  ) !important;
}
</style>
