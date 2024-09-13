import KassiopeiaTools from 'kassiopeia-tools'
import { BulmaCss } from './lib/BulmaCss'
import { anim as animTool, locker as lockerTool, toaster } from './lib/kassiopeia-tools'
import { configureDirectionButtons } from './utils/directionButtons'
import { formatDate, supportedLangs } from './utils/formatDate'

export class Startup {
  static #instance: Startup
  private readonly root = document.querySelector<HTMLDivElement>('#root')
  private readonly success = document.head.querySelector<HTMLElement>('meta[data-details="success"]')
  private readonly error = document.head.querySelector<HTMLElement>('meta[data-details="error"]')

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
          toaster.danger(val, 25000)
        }
      })
    }
  }

  private showSuccess() {
    if (this.success && this.success.dataset && this.success.dataset.message) {
      toaster.success(this.success.dataset.message, 25000)
    }

    const params = new URLSearchParams(location.search)
    if (params.size > 0) {
      params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'success') {
          toaster.success(val, 25000)
        }
      })
    }
  }

  private formatAllDate() {
    $<HTMLTimeElement>('time').each((_, element) => {
      if (element.dateTime && element.dataset.noFormatTime !== 'true') {
        const lang = supportedLangs.find((l) => l === element.dataset.timeLang)
        $(element).text(formatDate(new Date(element.dateTime), lang))
      }
    })

    $('[data-time-format]').each((_, element) => {
      if (element.textContent && element.dataset.noFormatTime !== 'true') {
        const lang = supportedLangs.find((l) => l === element.dataset.timeLang)
        $(element).text(formatDate(new Date(element.textContent), lang))
      }
    })
  }

  public run() {
    ;(globalThis as any).tools = KassiopeiaTools
    globalThis.locker = lockerTool
    globalThis.anim = animTool

    if (!this.root) console.error('Root container not found (404')

    this.configureBulmaCSS()
    this.configureMainMinHeight()

    configureDirectionButtons()

    this.showSuccess()
    this.showError()
    this.formatAllDate()

    $<HTMLInputElement>('input').on('blur', (e) => {
      if (['text', 'email', 'search'].indexOf(e.currentTarget.type) > -1) {
        e.currentTarget.value = e.currentTarget.value.trim()
      }
    })
  }

  public static get fast() {
    if (!Startup.#instance) {
      Startup.#instance = new Startup()
    }
    return Startup.#instance
  }
}
