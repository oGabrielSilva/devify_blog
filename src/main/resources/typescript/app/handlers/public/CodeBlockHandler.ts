import { generateHTML } from 'kassiopeia-tools'
import { requireHLJS } from '../../lib/highlight'
import { toaster } from '../../lib/kassiopeia-tools'
import { BaseHandler } from '../BaseHandler'

export class CodeBlockHandler extends BaseHandler {
  public handle(): void {
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
}
