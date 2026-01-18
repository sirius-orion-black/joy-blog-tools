<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.type_name') }}:</span>
        <a-input v-model:value="typeName" style="width: 150px; height: 32px" />
      </div>
      <div class="base-search-label">
        <a-button type="primary" @click="serchTypeList">{{ $t('menu.search') }}</a-button>
      </div>
    </div>
    <div class="base-container">
      <div>type</div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'

const typeName = ref<string>()

const serchTypeList = () => {}
</script>
