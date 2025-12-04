<script setup lang="ts">
import type { AIFlowyFormSchema } from '#/adapter/form';

import { computed, markRaw, onMounted, ref } from 'vue';

import { ProfileBaseSetting } from '@aiflowy/common-ui';

import { ElMessage } from 'element-plus';

import { api } from '#/api/request';
import Cropper from '#/components/upload/Cropper.vue';
import { $t } from '#/locales';
import { useAuthStore } from '#/store';

const { fetchUserInfo } = useAuthStore();
const profileBaseSettingRef = ref();

const formSchema = computed((): AIFlowyFormSchema[] => {
  return [
    {
      fieldName: 'avatar',
      component: markRaw(Cropper),
      componentProps: {
        crop: true,
      },
      label: $t('sysAccount.avatar'),
    },
    {
      fieldName: 'nickname',
      component: 'Input',
      label: $t('sysAccount.nickname'),
    },
    {
      fieldName: 'mobile',
      component: 'Input',
      label: $t('sysAccount.mobile'),
    },
    {
      fieldName: 'email',
      component: 'Input',
      label: $t('sysAccount.email'),
    },
  ];
});

onMounted(async () => {
  await getInfo();
});
async function getInfo() {
  loading.value = true;
  const data = await fetchUserInfo();
  await profileBaseSettingRef.value.getFormApi().setValues(data);
  loading.value = false;
}
const loading = ref(false);
const updateLoading = ref(false);
function handleSubmit(values: any) {
  updateLoading.value = true;
  api.post('/api/v1/sysAccount/updateProfile', values).then((res) => {
    updateLoading.value = false;
    if (res.errorCode === 0) {
      ElMessage.success($t('message.success'));
      getInfo();
    }
  });
}
</script>
<template>
  <ProfileBaseSetting
    v-loading="loading"
    :button-loading="updateLoading"
    ref="profileBaseSettingRef"
    :form-schema="formSchema"
    :button-text="$t('button.update')"
    @submit="handleSubmit"
  />
</template>
