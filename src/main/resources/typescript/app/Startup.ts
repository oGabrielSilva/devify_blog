import KassiopeiaTools, { generateHTML } from 'kassiopeia-tools'
import { ArticlePageHandler } from './handlers/public/ArticlePageHandler'
import { BulmaCss } from './lib/BulmaCss'
import { requireHLJS } from './lib/highlight'
import { anim as animTool, locker as lockerTool, toaster } from './lib/kassiopeia-tools'
import { configureDirectionButtons } from './utils/directionButtons'
import { formatDate, supportedLangs } from './utils/formatDate'

export class Startup {
  static #instance: Startup
  private readonly params = new URLSearchParams(location.search)

  private readonly root = document.querySelector<HTMLDivElement>('#root')
  private readonly info = document.head.querySelector<HTMLElement>('meta[data-details="info"]')
  private readonly warning = document.head.querySelector<HTMLElement>('meta[data-details="warning"]')
  private readonly success = document.head.querySelector<HTMLElement>('meta[data-details="success"]')
  private readonly error = document.head.querySelector<HTMLElement>('meta[data-details="error"]')

  private readonly metaPage = document.head.querySelector<HTMLElement>('meta[data-details="page"]')!

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
      toaster.danger(this.error.dataset.message, 50000)
    }

    if (this.params.size > 0) {
      this.params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'error') {
          toaster.danger(val, 50000)
        }
      })
    }
  }

  private showSuccess() {
    if (this.success && this.success.dataset && this.success.dataset.message) {
      toaster.success(this.success.dataset.message, 50000)
    }

    if (this.params.size > 0) {
      this.params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'success') {
          toaster.success(val, 50000)
        }
      })
    }
  }

  private showWarning() {
    if (this.warning && this.warning.dataset && this.warning.dataset.message) {
      toaster.warn(this.warning.dataset.message, 50000)
    }

    if (this.params.size > 0) {
      this.params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'warning') {
          toaster.warn(val, 50000)
        }
      })
    }
  }

  private showInfo() {
    if (this.info && this.info.dataset && this.info.dataset.message) {
      toaster.info(this.info.dataset.message, 50000)
    }

    if (this.params.size > 0) {
      this.params.forEach((val, key) => {
        if (key.toLocaleLowerCase() === 'info') {
          toaster.info(val, 50000)
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
    this.showWarning()
    this.showInfo()
    this.formatAllDate()

    $<HTMLInputElement>('input').on('blur', (e) => {
      if (['text', 'email', 'search'].indexOf(e.currentTarget.type) > -1) {
        e.currentTarget.value = e.currentTarget.value.trim()
      }
    })

    if (this.metaPage.dataset.content === 'article_page') {
      ArticlePageHandler.fast.handle()
    }

    document.querySelectorAll<HTMLElement>('*[class^="language-"]').forEach((el) => {
      if ($(el).closest('.editor-container').length == 0) {
        requireHLJS().highlightElement(el)

        const copyButton = generateHTML({
          tag: 'button',
          attributes: {
            type: 'button',
          },
          css: {
            position: 'absolute',
            top: '10px',
            right: '10px',
            fontSize: 'large',
          },
        })

        copyButton.onclick = () => {
          anim.otherAnimationByName(copyButton, 'animate__headShake')
          navigator.clipboard.writeText(el.innerText)
          toaster.info('Copiado!')
        }
        copyButton.innerHTML = '<span class="icon is-small"><i class="fa-solid fa-copy"></i></span>'
        el.parentElement!.appendChild(copyButton)
        el.parentElement!.style.position = 'relative'
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
