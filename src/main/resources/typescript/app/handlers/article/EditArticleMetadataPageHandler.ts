import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'
import { StackCreateForm } from '../stack/create/StackCreateForm'

export class EditArticleMetadataPageHandler extends BaseHandler {
  private readonly newStack = {
    handler: new StackCreateForm(),
    button: $('#new-stack-button'),
  }

  private readonly form = $<HTMLFormElement>('#metadata')
  private readonly titleInput = this.form.find<HTMLInputElement>('#title')
  private readonly metaDescriptionInput = this.form.find<HTMLTextAreaElement>('#article-meta-description')
  private readonly metaDescriptionSizeHelper = this.form.find<HTMLElement>('#article-meta-size')
  private readonly descriptionInput = this.form.find<HTMLInputElement>('#article-description-input')
  private readonly descriptionEditor = editors!.find((payload) => payload.id === 'article-description')!

  private isTitleValid = false
  private isMetaDescriptionValid = false

  private configureNewStackModule() {
    this.newStack.button.on('click', () => this.newStack.handler.openFormModal())
    this.newStack.handler.run()
  }

  private validateTitle() {
    const input = this.titleInput[0]
    this.isTitleValid = input.value.trim().length > 0

    if (this.isTitleValid) input.classList.remove('is-danger')
    else input.classList.add('is-danger')
  }

  private validateMetaDescription() {
    const input = this.metaDescriptionInput[0]
    this.isMetaDescriptionValid = input.value.length <= 225
    this.metaDescriptionSizeHelper.text(input.value.length)

    if (input.value.length > 160) {
      this.metaDescriptionInput.addClass('is-warning')
    }

    if (this.isMetaDescriptionValid) input.classList.remove('is-danger')
    else input.classList.add('is-danger')
  }

  private trySave() {
    this.validateTitle()
    this.validateMetaDescription()

    if (!this.isTitleValid) return dangerElement(this.titleInput[0], 'Título inválido')
    if (!this.isMetaDescriptionValid)
      return dangerElement(this.metaDescriptionInput[0], 'Meta descrição precisa ser válida')

    this.descriptionInput.val(this.descriptionEditor.editor.getHTML())
    this.save()
  }

  private save() {
    this.form[0].submit()
  }

  public handle(): void {
    this.configureNewStackModule()
    this.validateTitle()
    this.validateMetaDescription()

    this.titleInput.on('input', () => this.validateTitle())
    this.metaDescriptionInput.on('input', () => this.validateMetaDescription())

    this.form.on('submit', (e) => {
      e.preventDefault()
      this.trySave()
    })
  }
}
