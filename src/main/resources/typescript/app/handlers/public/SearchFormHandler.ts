import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'

export class PrimarySearchFormHandler extends BaseHandler {
  private readonly navButton = $('#search')
  private readonly searchForm = $<HTMLFormElement>('[data-id="search"]')
  private readonly hideButton = this.searchForm.find('.delete')

  private readonly queryInput = this.searchForm.find<HTMLInputElement>('input[type="search"]')

  private readonly queryMaxSize = Number(this.queryInput.attr('maxlength'))

  private isVisible = false

  private showSearchModal() {
    if (this.isVisible) return
    this.isVisible = true

    this.searchForm.slideDown()
    this.searchForm[0].style.display = 'flex'

    setTimeout(() => {
      this.searchForm.removeClass('is-hidden')
    }, 5)
  }

  private hideSearchModal() {
    if (!this.isVisible) return
    this.isVisible = false

    this.searchForm.slideUp({
      complete: () => {
        this.searchForm.addClass('is-hidden')
      },
    })
    this.searchForm[0].style.display = ''
  }

  private configureForm() {
    this.searchForm.on('submit', (e) => {
      e.preventDefault()

      const val = this.queryInput.val()!.trim()

      if (val.length > this.queryMaxSize) {
        return dangerElement(
          this.queryInput[0].parentElement!,
          `Tamanho máximo da query é ${this.queryMaxSize} caracteres`
        )
      }

      if (val.length < 4) {
        return dangerElement(this.queryInput[0].parentElement!, 'Seja mais detalhado')
      }

      this.queryInput.val(val)

      locker.lock()
      this.searchForm[0].submit()
    })
  }

  public handle(): void {
    this.navButton.on('click', () => this.showSearchModal())
    this.hideButton.on('click', () => this.hideSearchModal())

    this.configureForm()

    addEventListener('keydown', (e) => {
      if (e.key.toLowerCase() === 'escape' && this.isVisible) {
        e.preventDefault()
        this.hideSearchModal()
      }
    })

    console.log(this)
  }
}
