import { toaster } from '../lib/kassiopeia-tools'

export function dangerElement(element: HTMLElement, message?: string) {
  if (message) {
    toaster.danger(message)
  }
  anim.shakeX(element).addEventOnCompletion(() => anim.clean(element))
}
