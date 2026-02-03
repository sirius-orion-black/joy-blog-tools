//富文本编辑器
declare module '@wangeditor/editor-for-vue' {
  import { DefineComponent } from 'vue'
  import { IDomEditor } from '@wangeditor/editor'

  export const Editor: DefineComponent<{
    modelValue: string
    defaultConfig: Record<string, unknown>
    mode: 'default' | 'simple'
    bundle: false | true
    // onCreated: (editor: IDomEditor) => void
    style?: Record<string, string>
  }>

  export const Toolbar: DefineComponent<{
    editor: IDomEditor | null
    defaultConfig: Record<string, unknown>
    mode: 'default' | 'simple'
  }>
}
