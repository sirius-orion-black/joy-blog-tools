export interface FlowerState {
  //登录页面特效
  x: number
  y: number
  speed: number
  size: number
  rotation: number
  rotationSpeed: number
  swing: number
  swingSpeed: number
  swingRange: number
  color: string
  opacity: number
}

export interface LoginState {
  //登录表单
  username: string
  password: string
  remember: boolean
  move: number
  loginType: number
  nonceStr: string
}

export interface CaptchaState {
  //设置captcha参数类型
  moveX: number
  startX: number
  isMove: boolean
  posationX: number
  maxPositionX: number
}

export interface EmailState {
  username: string
  email: string
}
