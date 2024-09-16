import { type Editor } from '@tiptap/core'
import { openModal } from '../../../lib/BulmaCss'
import { dangerElement } from '../../../utils/dangerElement'

export class StackCreateForm {
  private readonly form: JQuery<HTMLFormElement>
  private readonly modal: HTMLElement
  private readonly metaDescriptionInput: JQuery<HTMLTextAreaElement>
  private readonly metaDescriptionSizeHelper: JQuery<HTMLElement>
  private readonly descriptionInput: JQuery<HTMLInputElement>
  private readonly descriptionEditorContainer: JQuery<HTMLElement>
  private readonly descriptionEditor: Editor

  public constructor(form: string | HTMLFormElement = '#stack-form') {
    if (typeof form === 'string' && !form.startsWith('#')) form = '#' + form
    this.form = $<HTMLFormElement>(form as HTMLFormElement)
    this.modal = this.form[0].querySelector('.modal') as HTMLElement
    this.metaDescriptionInput = this.form.find<HTMLTextAreaElement>('[name="metaDescription"]')
    this.metaDescriptionSizeHelper = this.form.find('[data-id="meta-size"]')
    this.descriptionInput = this.form.find<HTMLInputElement>('[name="description"]')
    this.descriptionEditorContainer = this.form.find('[data-editor-id]')

    this.descriptionEditor = editors!.find(
      (ed) =>
        ed.id === this.descriptionEditorContainer.attr('data-editor-id') &&
        ed.element === this.descriptionEditorContainer[0]
    )!.editor

    console.log(this)
  }

  private updateMetaDescriptionHelper() {
    const size = this.metaDescriptionInput.val()?.length ?? 0
    this.metaDescriptionInput.removeClass('is-warning')

    if (size > 160) {
      this.metaDescriptionInput.addClass('is-warning')
    }

    this.metaDescriptionSizeHelper.text(size)
  }

  private updateDescription() {
    this.descriptionInput.val(this.descriptionEditor.getHTML())
  }

  public openFormModal() {
    openModal(this.modal)
  }

  public run() {
    this.metaDescriptionInput.on('input', () => this.updateMetaDescriptionHelper())
    this.updateMetaDescriptionHelper()

    this.descriptionEditor.on('update', () => this.updateDescription())
    this.updateDescription()

    this.form.on('submit', (e) => {
      e.preventDefault()

      const idInput = this.form.find<HTMLInputElement>('[name="name"]')
      const id = idInput.val()

      if (!id || id.trim().length < 1) {
        dangerElement(idInput[0].parentElement!, 'Adicione um nome a sua Stack')
        return
      }
      if (id.length > 25) {
        dangerElement(idInput[0].parentElement!, 'Máximo de 25 caracteres')
        return
      }
      this.form[0].submit()
    })
  }
}
