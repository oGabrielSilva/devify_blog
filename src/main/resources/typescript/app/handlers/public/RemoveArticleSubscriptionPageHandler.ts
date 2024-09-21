import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'

export class RemoveArticleSubscriptionPageHandler extends BaseHandler {
  private readonly form = $<HTMLFormElement>('#subscription-notification-cancel')
  private readonly emailInput = this.form.find<HTMLInputElement>('input[type="email"]')

  public handle(): void {
    this.form.on('submit', (e) => {
      const email = this.emailInput.val()!

      if (email.length <= 5) {
        e.preventDefault()
        dangerElement(this.emailInput[0].parentElement!, 'Email muito curto')
        return
      }

      if (email.length > 150) {
        e.preventDefault()
        dangerElement(this.emailInput[0].parentElement!, 'Email muito grande')
        return
      }

      globalThis.locker.lock()
    })
  }
}
