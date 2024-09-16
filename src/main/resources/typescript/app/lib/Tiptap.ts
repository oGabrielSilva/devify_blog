import { Editor } from '@tiptap/core'
import { requireTiptapBasicExtensions } from './tiptap/extensions'
import { generateTopbar } from './tiptap/generateTopbar'
import { listenEditorChanges } from './tiptap/listenEditorChanges'

export class TiptapEditor {
  private readonly elements = document.querySelectorAll<HTMLElement>('.editor-container')

  private toGlobals(id: string, element: HTMLElement, editor: Editor) {
    if (!globalThis.editors) globalThis.editors = []
    globalThis.editors.push({
      id,
      editor,
      element,
    })
  }

  public run() {
    this.elements.forEach((container) => {
      const element = container.querySelector('.editor')!
      const bar = container.querySelector('.topbar')!

      if (!element) return console.error('Editor element not found')
      if (!bar) return console.error('Editor topbar not found')

      const content = element.innerHTML
      element.innerHTML = ''

      const editor = new Editor({
        element: element,
        extensions: requireTiptapBasicExtensions(),
        content,
        autofocus: true,
      })

      bar.innerHTML = ''
      const topbar = generateTopbar(editor)

      bar.appendChild(topbar.container)

      listenEditorChanges(editor, topbar)

      this.toGlobals(container.dataset.editorId ?? '', container, editor)
    })
  }

  public static get fast() {
    return new TiptapEditor()
  }
}
