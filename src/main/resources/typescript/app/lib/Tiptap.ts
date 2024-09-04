import { Editor } from '@tiptap/core'
import { requireTiptapBasicExtensions } from './tiptap/extensions'

export class TiptapEditor {
  private readonly element = document.querySelector('#editor') as HTMLElement
  private readonly editor: Editor

  constructor() {
    const content = this.element.innerHTML
    this.element.innerHTML = ''

    this.editor = new Editor({
      element: this.element,
      extensions: requireTiptapBasicExtensions(),
      content,
      autofocus: true,
    })
  }

  public run() {}

  public static get fast() {
    return new TiptapEditor()
  }
}
