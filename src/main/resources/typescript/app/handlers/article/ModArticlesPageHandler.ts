import { openModal } from '../../lib/BulmaCss'
import { BaseHandler } from '../BaseHandler'

export class ModArticlesPageHandler extends BaseHandler {
  private readonly btnOpenModal = $('#open-form')
  private readonly modal = $('#state-modal')

  public handle(): void {
    this.btnOpenModal.on('click', () => openModal(this.modal[0]))
  }
}
