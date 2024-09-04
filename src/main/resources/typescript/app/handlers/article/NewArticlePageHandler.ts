import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'
import { StackCreateForm } from '../stack/create/StackCreateForm'

export class NewArticlePageHandler extends BaseHandler {
  private readonly newStack = {
    handler: new StackCreateForm(),
    button: $('#new-stack-button'),
  }

  private readonly form = $<HTMLFormElement>('#new-article')
  private readonly titleInput = $<HTMLInputElement>('#title')
  // private readonly keywordsUserInput = $<HTMLInputElement>('#keywords')
  // private readonly keywordsInput = $<HTMLInputElement>('input[name="keywords"]')
  // private readonly keywordsPreview = $<HTMLDivElement>('#keywords-preview')
  // private readonly addKeywordBtn = $('#add-keyword-btn')

  private isTitleValid = false
  private readonly keywords = new Set<string>()

  private configureNewStackModule() {
    this.newStack.button.on('click', () => this.newStack.handler.openFormModal())
    this.newStack.handler.run()
  }

  private validateTitle() {
    const input = this.titleInput[0]
    const value = input.value

    if (value && value.trim().length > 0) {
      input.classList.remove('is-danger')
      this.isTitleValid = true
    } else {
      input.classList.add('is-danger')
      this.isTitleValid = false
    }
  }

  public handle(): void {
    this.configureNewStackModule()

    this.validateTitle()
    this.titleInput.removeClass('is-danger')
    this.titleInput.on('input', () => this.validateTitle())

    this.form.on('submit', (e) => {
      e.preventDefault()

      this.validateTitle()

      if (!this.isTitleValid) {
        return dangerElement(this.titleInput[0], 'Informe um título')
      }

      e.currentTarget.submit()
    })
  }
}
