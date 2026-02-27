import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export const useDynamicLabels = () => {
  const { t } = useI18n()

  //表格列表
  const columns = computed(() => [
    {
      title: t('columns.article_title'),
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: t('columns.state'),
      dataIndex: 'state',
      key: 'state',
    },
    {
      title: t('columns.cover'),
      dataIndex: 'cover',
      key: 'cover',
    },
    {
      title: t('columns.read_type'),
      dataIndex: 'readType',
      key: 'readType',
    },

    {
      title: t('columns.classify_name'),
      dataIndex: 'classifyName',
      key: 'classifyName',
    },
    {
      title: t('columns.label_name'),
      dataIndex: 'labelNames',
      key: 'labelNames',
    },
    {
      title: t('columns.keywords'),
      dataIndex: 'keywords',
      key: 'keywords',
    },
    {
      title: t('columns.introduction'),
      dataIndex: 'introduction',
      key: 'introduction',
      width: 250,
      ellipsis: true,
    },
    {
      title: t('columns.author'),
      dataIndex: 'userName',
      key: 'userName',
      ellipsis: true,
    },
    {
      title: t('columns.action'),
      key: 'operation',
      fixed: 'right',
      width: 105,
    },
  ])

  //状态
  const stateOptions = computed(() => [
    {
      label: t('columns.shelve'),
      value: 1,
    },
    {
      label: t('columns.unShelve'),
      value: 2,
    },
    {
      label: t('columns.reviewing'),
      value: 4,
    },
    {
      label: t('columns.returned'),
      value: 5,
    },
    {
      label: t('columns.offline'),
      value: 6,
    },
    {
      label: t('columns.archived'),
      value: 7,
    },
  ])
  //根据 value 获取对应的 label
  const getStateByValue = (value: number) => stateOptions.value.find((opt) => opt.value === value)?.label || ''

  //阅读类型
  const readTypeOptions = computed(() => [
    {
      label: t('columns.free_reading'),
      value: 1,
    },
    {
      label: t('columns.paid_reading'),
      value: 2,
    },
    {
      label: t('columns.member_reading'),
      value: 3,
    },
    {
      label: t('columns.private'),
      value: 4,
    },
    {
      label: t('columns.login_reading'),
      value: 5,
    },
  ])
  const getReadTypeByValue = (value: number) => readTypeOptions.value.find((opt) => opt.value === value)?.label || ''

  const classNames: string[] = ['a66cff', 'f9a11b', 'ffa1cf', 'c8aabcc', 'c9c9efe', 'c662b2f']

  return { columns, stateOptions, readTypeOptions, classNames, getStateByValue, getReadTypeByValue }
}
