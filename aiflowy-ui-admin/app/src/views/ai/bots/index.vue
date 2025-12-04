<script setup lang="ts">
import type { BotInfo } from '@aiflowy/types';

import type { ActionButton } from '#/components/page/CardList.vue';

import { markRaw, ref } from 'vue';
import { useRouter } from 'vue-router';

import { $t } from '@aiflowy/locales';

import {
  Delete,
  Edit,
  Plus,
  Setting,
  VideoPlay,
} from '@element-plus/icons-vue';

import defaultAvatar from '#/assets/ai/bot/defaultBotAvatar.png';
import HeaderSearch from '#/components/headerSearch/HeaderSearch.vue';
import CardList from '#/components/page/CardList.vue';
import PageData from '#/components/page/PageData.vue';

import Modal from './modal.vue';

const router = useRouter();
const pageDataRef = ref();
const modalRef = ref<InstanceType<typeof Modal>>();

// 操作按钮配置
const headerButtons = [
  {
    key: 'create',
    text: $t('button.create'),
    icon: markRaw(Plus),
    type: 'primary',
    data: { action: 'create' },
    permission: '/api/v1/aiKnowledge/save',
  },
];
const actions: ActionButton[] = [
  {
    icon: Edit,
    text: $t('button.edit'),
    className: '',
    permission: '',
    onClick(row: BotInfo) {
      modalRef.value?.open('edit', row);
    },
  },
  {
    icon: Setting,
    text: $t('button.setting'),
    className: '',
    permission: '',
    onClick(row: BotInfo) {
      router.push({ path: `/ai/bots/setting/${row.id}` });
    },
  },
  {
    icon: VideoPlay,
    text: $t('button.run'),
    className: '',
    permission: '',
    onClick(row: BotInfo) {
      router.push({ path: `/ai/bots/run/${row.id}` });
      // 打开新窗口
      // window.open(`/ai/bots/run/${item.id}`, '_blank');
    },
  },
  {
    icon: Delete,
    text: $t('button.delete'),
    className: '',
    permission: '/api/v1/aiBot/remove',
    onClick(row: BotInfo) {
      console.log(row);
    },
  },
];

const handleSearch = (params: string) => {
  pageDataRef.value.setQuery({ title: params, isQueryOr: true });
};
const handleButtonClick = () => {
  modalRef.value?.open('create');
};
</script>

<template>
  <div class="space-y-5 p-5">
    <HeaderSearch
      :buttons="headerButtons"
      @search="handleSearch"
      @button-click="handleButtonClick"
    />
    <PageData
      ref="pageDataRef"
      page-url="/api/v1/aiBot/page"
      :init-query-params="{ status: 1 }"
    >
      <template #default="{ pageList }">
        <CardList
          :default-icon="defaultAvatar"
          :data="pageList"
          :actions="actions"
        />
      </template>
    </PageData>

    <Modal ref="modalRef" @success="pageDataRef.setQuery({})" />
  </div>
</template>
