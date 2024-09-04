import { BulmaCss } from './lib/BulmaCss'
import { anim as animTool, locker as lockerTool, toaster } from './lib/kassiopeia-tools'
import { configureDirectionButtons } from './utils/directionButtons'
import { formatDate, supportedLangs } from './utils/formatDate'

export class Startup {
  static #instance: Startup
  private readonly root = document.querySelector<HTMLDivElement>('#root')
  private readonly error = document.head.querySelector<HTMLMetaElement>('meta[data-details="error"]')

  private configureBulmaCSS() {
    const bulma = new BulmaCss()
    bulma.animateNavMenu()
    bulma.listenAllThemeButton()
    bulma.updateColorScheme(bulma.recoveryDefinedColorScheme())
    bulma.listenAllGoBackModalButton()
  }

  private configureMainMinHeight() {
    const header = document.querySelector('#header')
    const headerH = header ? header.clientHeight : 0

    // const footer = document.querySelector('#footer')
    // const footerH = footer ? footer.clientHeight : 0

    if (this.root) this.root!.style.minHeight = `calc(100vh - ${headerH}px)`
  }

  private showError() {
    if (this.error && this.error.dataset && this.error.dataset.message) {
      toaster.danger(this.error.dataset.message, 25000)
    }

    const params = new URLSearchParams(location.search)
    if (params.size > 0) {
      params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'error') {
          toaster.danger(val)
        }
      })
    }
  }

  private formatAllDate() {
    $<HTMLTimeElement>('time').each((_, element) => {
      if (element.dateTime) {
        const lang = supportedLangs.find((l) => l === element.dataset.timeLang)
        $(element).text(formatDate(new Date(element.dateTime), lang))
      }
    })

    $('[data-time-format]').each((_, element) => {
      if (element.textContent) {
        const lang = supportedLangs.find((l) => l === element.dataset.timeLang)
        $(element).text(formatDate(new Date(element.textContent), lang))
      }
    })
  }

  public run() {
    globalThis.locker = lockerTool
    globalThis.anim = animTool

    if (!this.root) console.error('Root container not found (404')

    this.configureBulmaCSS()
    this.configureMainMinHeight()

    configureDirectionButtons()

    this.showError()
    this.formatAllDate()
  }

  public static get fast() {
    if (!Startup.#instance) {
      Startup.#instance = new Startup()
    }
    return Startup.#instance
  }
}
