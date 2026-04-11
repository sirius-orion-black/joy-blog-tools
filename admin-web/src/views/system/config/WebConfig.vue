<template>
  <div class="base-main">
    <div class="web-config-content">
      <a-tabs :activeKey="activeKey" @change="handleTapChange">
        <a-tab-pane v-for="(configs, key) in info" :key="key" :tab="$t(`config.${key}`)">
          <div class="config-ui">
            <div class="config-li base-bg-shadow" v-for="item in configs" :key="item.id!">
              <div class="li-title">{{ item.configName }}</div>
              <div class="ui-content">
                <span class="ui-title">{{ $t('config.name') }}:</span>
                <span class="ui-input">
                  <a-input name="configName" v-model:value="item.configName" />
                </span>
              </div>
              <div class="ui-content">
                <span class="ui-title">{{ $t('config.value') }}:</span>
                <span class="ui-input">
                  <a-input name="configValue" v-model:value="item.configValue" />
                </span>
              </div>
              <div class="ui-content">
                <span class="ui-title">{{ $t('config.sort') }}:</span>
                <span class="ui-input">
                  <a-input-number name="configSort" v-model:value="item.configSort" />
                </span>
              </div>
              <div class="ui-content">
                <span class="ui-title">{{ $t('config.state') }}:</span>
                <span class="ui-input">
                  <a-radio-group v-model:value="item.configState">
                    <a-radio-button :value="1">{{ $t('columns.normal') }}</a-radio-button>
                    <a-radio-button :value="2">{{ $t('columns.disabled') }}</a-radio-button>
                  </a-radio-group>
                </span>
              </div>
              <div class="ui-content" v-if="key === 'file_type'">
                <span class="ui-title">{{ $t('config.restrict') }}:</span>
                <span class="ui-input">
                  <a-input name="configRestrict" v-model:value="item.configRestrict" />
                </span>
              </div>
              <div class="ui-button">
                <a-button type="primary" @click="submitInfo(item)">{{ $t('menu.submit') }}</a-button>
              </div>
            </div>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { configStore } from '@/stores/config'
import type { ConfigState } from '@/types/configType'

const activeKey = ref('')
const config = configStore()

const info = ref<Record<string, ConfigState[]>>({})

const handleTapChange = (key: string) => {
  activeKey.value = key
}
const getConfigList = () => {
  config.getConfig().then((rs) => {
    info.value = rs
    for (const key in rs) {
      if (rs.hasOwnProperty(key)) {
        activeKey.value = key
        return
      }
    }
  })
}
onMounted(() => {
  getConfigList()
})
const submitInfo = (val: ConfigState) => {
  if (!val.id) return
  config.editConfig(val).then(() => {
    // getConfigList()
  })
}
</script>
<style lang="scss" scoped>
.web-config-content {
  .config-ui {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-around;
    width: 100%;
    overflow: hidden;
    padding: 15px 5px;
    .config-li {
      flex: 1 1 500px;
      min-width: 0;
      margin: 0 20px 30px;
      border-radius: 12px;
      padding: 15px;
      overflow: hidden;
      .li-title {
        text-align: center;
        line-height: 38px;
        padding-bottom: 10px;
      }
      .ui-content {
        display: flex;
        padding: 10px 0;
        .ui-title {
          width: 120px;
        }
        .ui-input {
          flex: 1;
        }
      }
      .ui-button {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 60px;
      }
    }
  }
}
</style>
