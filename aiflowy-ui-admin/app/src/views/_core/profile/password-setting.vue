<script setup lang="ts">
import type { AIFlowyFormSchema } from '#/adapter/form';

import { computed, ref } from 'vue';

import { ProfilePasswordSetting, z } from '@aiflowy/common-ui';

import { ElMessage } from 'element-plus';

import { api } from '#/api/request';
import { $t } from '#/locales';

const profilePasswordSettingRef = ref();

const formSchema = computed((): AIFlowyFormSchema[] => {
  return [
    {
      fieldName: 'password',
      label: $t('sysAccount.oldPwd'),
      component: 'AIFlowyInputPassword',
      componentProps: {
        placeholder: $t('sysAccount.oldPwd') + $t('common.isRequired'),
      },
    },
    {
      fieldName: 'newPassword',
      label: $t('sysAccount.newPwd'),
      component: 'AIFlowyInputPassword',
      componentProps: {
        passwordStrength: true,
        placeholder: $t('sysAccount.newPwd') + $t('common.isRequired'),
      },
    },
    {
      fieldName: 'confirmPassword',
      label: $t('sysAccount.confirmPwd'),
      component: 'AIFlowyInputPassword',
      componentProps: {
        passwordStrength: true,
        placeholder: $t('sysAccount.repeatPwd'),
      },
      dependencies: {
        rules(values) {
          const { newPassword } = values;
          return z
            .string({ required_error: $t('sysAccount.repeatPwd') })
            .min(1, { message: $t('sysAccount.repeatPwd') })
            .refine((value) => value === newPassword, {
              message: $t('sysAccount.notSamePwd'),
            });
        },
        triggerFields: ['newPassword'],
      },
    },
  ];
});

const updateLoading = ref(false);
function handleSubmit(values: any) {
  updateLoading.value = true;
  api.post('/api/v1/sysAccount/updatePassword', values).then((res) => {
    updateLoading.value = false;
    if (res.errorCode === 0) {
      ElMessage.success($t('message.success'));
    }
  });
}
</script>
<template>
  <ProfilePasswordSetting
    :button-loading="updateLoading"
    :button-text="$t('button.update')"
    ref="profilePasswordSettingRef"
    class="w-1/3"
    :form-schema="formSchema"
    @submit="handleSubmit"
  />
</template>
