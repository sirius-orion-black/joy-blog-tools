export interface ConfigState {
  id: number | null //id
  configName: string //名称
  configType: number | null //类型
  configKey: number | null //键
  configValue: string //值
  configSort: number //排序
  configState: number //状态
  configRestrict: string //约束
}
