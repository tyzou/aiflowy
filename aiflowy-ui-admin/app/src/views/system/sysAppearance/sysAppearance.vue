<script setup lang="ts">
import type { Component } from 'vue';

import type { BuiltinThemePreset } from '@aiflowy/preferences';
import type {
  BuiltinThemeType,
  LayoutType,
  ThemeModeType,
} from '@aiflowy/types';

import { computed, ref } from 'vue';

import {
  CircleHelp,
  MoonStar,
  Sun,
  SunMoon,
  UserRoundPen,
} from '@aiflowy/icons';
import {
  FullContent,
  HeaderMixedNav,
  HeaderNav,
  HeaderSidebarNav,
  MixedNav,
  SidebarMixedNav,
  SidebarNav,
} from '@aiflowy/layouts';
import { BUILT_IN_THEME_PRESETS } from '@aiflowy/preferences';
import { convertToHsl, TinyColor } from '@aiflowy/utils';

import { AIFlowyTooltip } from '@aiflowy-core/shadcn-ui';

import { useThrottleFn } from '@vueuse/core';
import { ElInputNumber, ElSwitch } from 'element-plus';

import { $t } from '#/locales';

interface PresetItem {
  name: string;
  tip: string;
  type: LayoutType;
}

const model = ref('auto');
const color = ref<BuiltinThemeType>('default');
const themeColorPrimary = ref<string>('themeColorPrimary');
const colorInput = ref();
const layout = ref<LayoutType>('sidebar-nav');

const THEME_PRESET: Array<{ icon: Component; name: ThemeModeType }> = [
  {
    icon: Sun,
    name: 'light',
  },
  {
    icon: MoonStar,
    name: 'dark',
  },
  {
    icon: SunMoon,
    name: 'auto',
  },
];
const builtinThemePresets = computed(() => {
  return [...BUILT_IN_THEME_PRESETS];
});
const inputValue = computed(() => {
  return new TinyColor(themeColorPrimary.value || '').toHexString();
});
const PRESET = computed((): PresetItem[] => [
  {
    name: $t('preferences.vertical'),
    tip: $t('preferences.verticalTip'),
    type: 'sidebar-nav',
  },
  {
    name: $t('preferences.horizontal'),
    tip: $t('preferences.horizontalTip'),
    type: 'header-nav',
  },
  {
    name: $t('preferences.mixedMenu'),
    tip: $t('preferences.mixedMenuTip'),
    type: 'mixed-nav',
  },
]);
const components: Record<LayoutType, Component> = {
  'full-content': FullContent,
  'header-nav': HeaderNav,
  'mixed-nav': MixedNav,
  'sidebar-mixed-nav': SidebarMixedNav,
  'sidebar-nav': SidebarNav,
  'header-mixed-nav': HeaderMixedNav,
  'header-sidebar-nav': HeaderSidebarNav,
};

const updateThemeColorPrimary = useThrottleFn(
  (value: string) => {
    themeColorPrimary.value = value;
  },
  300,
  true,
  true,
);

function activeClass(theme: string): string[] {
  return theme === model.value ? ['outline-box-active'] : [];
}
function nameView(name: string) {
  switch (name) {
    case 'auto': {
      return $t('preferences.followSystem');
    }
    case 'dark': {
      return $t('preferences.theme.dark');
    }
    case 'light': {
      return $t('preferences.theme.light');
    }
  }
}
function typeView(name: BuiltinThemeType) {
  switch (name) {
    case 'custom': {
      return $t('preferences.theme.builtin.custom');
    }
    case 'deep-blue': {
      return $t('preferences.theme.builtin.deepBlue');
    }
    case 'deep-green': {
      return $t('preferences.theme.builtin.deepGreen');
    }
    case 'default': {
      return $t('preferences.theme.builtin.default');
    }
    case 'gray': {
      return $t('preferences.theme.builtin.gray');
    }
    case 'green': {
      return $t('preferences.theme.builtin.green');
    }

    case 'neutral': {
      return $t('preferences.theme.builtin.neutral');
    }
    case 'orange': {
      return $t('preferences.theme.builtin.orange');
    }
    case 'pink': {
      return $t('preferences.theme.builtin.pink');
    }
    case 'rose': {
      return $t('preferences.theme.builtin.rose');
    }
    case 'sky-blue': {
      return $t('preferences.theme.builtin.skyBlue');
    }
    case 'slate': {
      return $t('preferences.theme.builtin.slate');
    }
    case 'violet': {
      return $t('preferences.theme.builtin.violet');
    }
    case 'yellow': {
      return $t('preferences.theme.builtin.yellow');
    }
    case 'zinc': {
      return $t('preferences.theme.builtin.zinc');
    }
  }
}
function handleSelect(theme: BuiltinThemePreset) {
  color.value = theme.type;
}
function selectColor() {
  colorInput.value?.[0]?.click?.();
}
function handleInputChange(e: Event) {
  const target = e.target as HTMLInputElement;
  updateThemeColorPrimary(convertToHsl(target.value));
}
</script>

<template>
  <div class="mx-auto flex w-full max-w-[900px] flex-col gap-4 p-5">
    <!-- 主题与配色 -->
    <div
      class="bg-background border-border flex flex-col gap-6 rounded-lg border p-5 pb-6"
    >
      <div class="text-base font-medium">主题与配色</div>
      <!-- 主题模式 -->
      <div class="flex flex-col gap-2.5">
        <span class="text-sm font-medium">主题模式</span>
        <div class="flex items-center gap-5">
          <template v-for="theme in THEME_PRESET" :key="theme.name">
            <div class="flex cursor-pointer flex-col">
              <div
                :class="activeClass(theme.name)"
                class="outline-box flex-center py-4"
              >
                <component :is="theme.icon" class="mx-9 size-5" />
              </div>
              <div class="text-muted-foreground mt-2 text-center text-xs">
                {{ nameView(theme.name) }}
              </div>
            </div>
          </template>
        </div>
      </div>

      <!-- 主题色 -->
      <div class="flex flex-col gap-2.5">
        <span class="text-sm font-medium">主题色</span>
        <div class="flex flex-wrap items-center gap-5">
          <template v-for="theme in builtinThemePresets" :key="theme.type">
            <div
              class="flex cursor-pointer flex-col"
              @click="handleSelect(theme)"
            >
              <div
                :class="{
                  'outline-box-active': theme.type === color,
                }"
                class="outline-box flex-center group cursor-pointer"
              >
                <template v-if="theme.type !== 'custom'">
                  <div
                    :style="{ backgroundColor: theme.color }"
                    class="mx-9 my-2 size-5 rounded-md"
                  ></div>
                </template>
                <template v-else>
                  <div class="size-full px-9 py-2" @click.stop="selectColor">
                    <div class="flex-center relative size-5 rounded-sm">
                      <UserRoundPen
                        class="z-1 absolute size-5 opacity-60 group-hover:opacity-100"
                      />
                      <input
                        ref="colorInput"
                        :value="inputValue"
                        class="absolute inset-0 opacity-0"
                        type="color"
                        @input="handleInputChange"
                      />
                    </div>
                  </div>
                </template>
              </div>
              <div class="text-muted-foreground my-2 text-center text-xs">
                {{ typeView(theme.type) }}
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 布局与导航 -->
    <div
      class="bg-background border-border flex flex-col gap-6 rounded-lg border p-5 pb-6"
    >
      <div class="text-base font-medium">布局与导航</div>
      <!-- 布局模式 -->
      <div class="flex flex-col gap-2.5">
        <span class="text-sm font-medium">布局模式</span>
        <div class="flex items-center gap-5">
          <template v-for="theme in PRESET" :key="theme.name">
            <div
              class="flex w-[100px] cursor-pointer flex-col"
              @click="layout = theme.type"
            >
              <div
                :class="activeClass(theme.type)"
                class="outline-box flex-center"
              >
                <component :is="components[theme.type]" />
              </div>
              <div
                class="text-muted-foreground flex-center hover:text-foreground mt-2 text-center text-xs"
              >
                {{ theme.name }}
                <AIFlowyTooltip v-if="theme.tip" side="bottom">
                  <template #trigger>
                    <CircleHelp class="ml-1 size-3 cursor-help" />
                  </template>
                  {{ theme.tip }}
                </AIFlowyTooltip>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 界面显示 -->
    <div
      class="bg-background border-border flex flex-col gap-6 rounded-lg border p-5 pb-6"
    >
      <div class="text-base font-medium">界面显示</div>
      <!-- 页面标签页 -->
      <div class="flex flex-col gap-2.5">
        <span class="text-sm font-medium">页面标签页</span>
        <div class="flex items-center gap-5">
          <div class="flex items-center gap-3.5">
            <span class="text-muted-foreground text-xs">{{
              $t('preferences.tabbar.enable')
            }}</span>
            <ElSwitch />
          </div>
          <div class="flex items-center gap-3.5">
            <span class="text-muted-foreground text-xs">{{
              $t('preferences.tabbar.draggable')
            }}</span>
            <ElSwitch />
          </div>
          <div class="flex items-center gap-3.5">
            <span class="text-muted-foreground text-xs">{{
              $t('preferences.tabbar.icon')
            }}</span>
            <ElSwitch />
          </div>
          <div class="flex items-center gap-3.5">
            <span class="text-muted-foreground text-xs">{{
              $t('preferences.tabbar.maxCount')
            }}</span>
            <ElInputNumber />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
