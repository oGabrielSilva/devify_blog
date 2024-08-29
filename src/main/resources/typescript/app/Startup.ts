import { BulmaCss } from './lib/BulmaCss'
import { anim, locker, toaster } from './lib/kassiopeia-tools'
import { configureDirectionButtons } from './utils/directionButtons'

export class Startup {
  static #instance: Startup
  private readonly root = document.querySelector<HTMLDivElement>('#root')
  private readonly error = document.head.querySelector<HTMLMetaElement>('meta[data-details="error"]')

  private configureBulmaCSS() {
    const bulma = new BulmaCss()
    bulma.animateNavMenu()
    bulma.listenAllThemeButton()
    bulma.updateColorScheme(bulma.recoveryDefinedColorScheme())
  }

  private configureMainMinHeight() {
    const header = document.querySelector('#header')
    const footer = document.querySelector('#footer')

    if (header && footer) {
      this.root!.style.minHeight = `calc(100vh - ${header!.clientHeight}px - ${footer!.clientHeight / 2}px)`
    }
  }

  private showError() {
    if (this.error && this.error.dataset && this.error.dataset.message) {
      toaster.danger(this.error.dataset.message, 25000)
    }
  }

  public run() {
    if (!this.root) console.error('Root container not found (404')

    this.configureBulmaCSS()
    this.configureMainMinHeight()

    configureDirectionButtons()

    this.showError()
    self.locker = locker
    self.anim = anim
  }

  public static get fast() {
    if (!Startup.#instance) {
      Startup.#instance = new Startup()
    }
    return Startup.#instance
  }
}
