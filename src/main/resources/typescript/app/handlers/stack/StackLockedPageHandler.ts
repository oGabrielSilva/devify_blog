import { openModal } from '../../lib/BulmaCss'
import { BaseHandler } from '../BaseHandler'

export class StackLockedPageHandler extends BaseHandler {
  private readonly buttonsOpenDescriptionModal = $('[data-description]')

  public handle(): void {
    this.buttonsOpenDescriptionModal.each((_, btn) => {
      const modal = document.querySelector(
        `[data-description-id="${btn.dataset.description}"]`
      ) as HTMLElement

      $(btn).on('click', () => openModal(modal))
    })
  }
}
