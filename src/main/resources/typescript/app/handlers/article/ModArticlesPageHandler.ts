import { openModal } from '../../lib/BulmaCss'
import { BaseHandler } from '../BaseHandler'

export class ModArticlesPageHandler extends BaseHandler {
  private readonly btnOpenModal = $('#open-form')
  private readonly modal = $('#state-modal')
  private readonly form = $('#change-state')

  public handle(): void {
    this.btnOpenModal.on('click', () => openModal(this.modal[0]))

    this.form.on('submit', () => globalThis.locker.lock())
  }
}
