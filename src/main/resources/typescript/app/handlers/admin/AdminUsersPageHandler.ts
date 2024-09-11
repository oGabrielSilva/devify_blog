import { openModal } from '../../lib/BulmaCss'
import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'

export class AdminUsersPageHandler extends BaseHandler {
  private readonly buttonUpdate = $<HTMLButtonElement>('button[data-modal]')
  private readonly authorityForms = $<HTMLFormElement>('form[data-authority]')
  private readonly stateForms = $<HTMLFormElement>('form[data-state]')

  public handle(): void {
    this.buttonUpdate.each((_, button) => {
      const id = button.dataset.modal
      const modal = document.querySelector(`div[data-modal-id="${id}"]`) as HTMLElement
      $(button).on('click', () => openModal(modal))
    })

    this.authorityForms.each((_, form) => {
      $(form).on('submit', () => locker.lock())
    })

    this.stateForms.each((_, form) => {
      const input = $(form).find<HTMLInputElement>('input[name="reason"]')
      if (!input[0]) {
        $(form).on('submit', () => locker.lock())
        return
      }

      $(form).on('submit', (e) => {
        e.preventDefault()
        if (input.val()!.trim().length < 1)
          return dangerElement(input[0].parentElement!, 'Adicione uma mensagem para o registro da ação')

        locker.lock()
        form.submit()
      })
    })
  }
}
