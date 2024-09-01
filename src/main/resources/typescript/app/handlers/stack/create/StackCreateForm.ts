import { openModal } from '../../../lib/BulmaCss'
import { dangerElement } from '../../../utils/dangerElement'

export class StackCreateForm {
  private readonly form: JQuery<HTMLFormElement>
  private readonly modal: HTMLElement
  private readonly metaDescriptionInput: JQuery<HTMLTextAreaElement>
  private readonly metaDescriptionSizeHelper: JQuery<HTMLElement>

  public constructor(form: string | HTMLFormElement = '#stack-form') {
    if (typeof form === 'string' && !form.startsWith('#')) form = '#' + form
    this.form = $<HTMLFormElement>(form as HTMLFormElement)
    this.modal = this.form[0].querySelector('.modal') as HTMLElement
    this.metaDescriptionInput = $<HTMLTextAreaElement>('#meta-description')
    this.metaDescriptionSizeHelper = $('#meta-size')
  }

  private updateMetaDescriptionHelper() {
    const size = this.metaDescriptionInput.val()?.length ?? 0
    this.metaDescriptionInput.removeClass('is-warning')

    if (size > 160) {
      this.metaDescriptionInput.addClass('is-warning')
    }

    this.metaDescriptionSizeHelper.text(size)
  }

  public openFormModal() {
    openModal(this.modal)
  }

  public run() {
    this.metaDescriptionInput.on('input', () => this.updateMetaDescriptionHelper())
    this.updateMetaDescriptionHelper()

    this.form.on('submit', (e) => {
      e.preventDefault()

      const id = this.form.find<HTMLInputElement>('#stack-name').val()

      if (!id || id.trim().length < 1) {
        dangerElement(this.form[0].querySelector('#stack-name')!, 'Adicione um nome a sua Stack')
        return
      }
      if (id.length > 25) {
        dangerElement(this.form[0].querySelector('#stack-name')!, 'Máximo de 25 caracteres')
        return
      }
      this.form[0].submit()
    })
  }
}
