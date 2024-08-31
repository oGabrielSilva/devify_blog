import { AnimationKassiopeiaTool, ScreenLockerKassiopeiaTool } from 'kassiopeia-tools'

export {}
declare global {
  interface Array<T> {
    pickRandom(): T | null
  }

  var locker: ScreenLockerKassiopeiaTool
  var anim: AnimationKassiopeiaTool

  type AppScheme = 'dark' | 'light'
}