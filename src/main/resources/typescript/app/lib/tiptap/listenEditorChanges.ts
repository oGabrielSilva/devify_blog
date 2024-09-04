import { type Editor } from '@tiptap/core'
import { type Topbar } from './generateTopbar'

export function listenEditorChanges(editor: Editor, topbar: Topbar) {
  const fn = () => {
    if (editor.isActive('bold')) {
      $(topbar.bold).addClass('has-text-link')
    } else $(topbar.bold).removeClass('has-text-link')

    if (editor.isActive('italic')) {
      $(topbar.italic).addClass('has-text-link')
    } else $(topbar.italic).removeClass('has-text-link')

    if (editor.isActive('underline')) {
      $(topbar.underline).addClass('has-text-link')
    } else $(topbar.underline).removeClass('has-text-link')

    if (editor.isActive('strike')) {
      $(topbar.strike).addClass('has-text-link')
    } else $(topbar.strike).removeClass('has-text-link')

    if (editor.isActive('heading', { level: 1 })) {
      $(topbar.h1).addClass('has-text-link')
    } else $(topbar.h1).removeClass('has-text-link')

    if (editor.isActive('heading', { level: 2 })) {
      $(topbar.h2).addClass('has-text-link')
    } else $(topbar.h2).removeClass('has-text-link')

    if (editor.isActive('orderedList')) {
      $(topbar.ol).addClass('has-text-link')
    } else $(topbar.ol).removeClass('has-text-link')

    if (editor.isActive('bulletList')) {
      $(topbar.ul).addClass('has-text-link')
    } else $(topbar.ul).removeClass('has-text-link')

    const color = editor.getAttributes('textStyle').color
    if (color) {
      topbar.colorPicker.style.color = color
      $(topbar.colorPicker).find('input').val(color)
    } else {
      topbar.colorPicker.style.color = ''
      $(topbar.colorPicker).find('input').val('#000000')
    }

    const highlight = editor.getAttributes('highlight')
    if (highlight.color) {
      topbar.bgPicker.style.color = highlight.color
      $(topbar.bgPicker).find('input').val(highlight.color)
    } else {
      topbar.bgPicker.style.color = ''
      $(topbar.bgPicker).find('input').val('#000000')
    }

    if (editor.isActive({ textAlign: 'left' })) {
      $(topbar.alignLeft).addClass('has-text-link')
    } else $(topbar.alignLeft).removeClass('has-text-link')

    if (editor.isActive({ textAlign: 'center' })) {
      $(topbar.alignCenter).addClass('has-text-link')
    } else $(topbar.alignCenter).removeClass('has-text-link')

    if (editor.isActive({ textAlign: 'right' })) {
      $(topbar.alignRight).addClass('has-text-link')
    } else $(topbar.alignRight).removeClass('has-text-link')

    if (editor.isActive({ textAlign: 'justify' })) {
      $(topbar.alignJustify).addClass('has-text-link')
    } else $(topbar.alignJustify).removeClass('has-text-link')

    if (editor.isActive('blockquote')) {
      $(topbar.blockquote).addClass('has-text-link')
    } else $(topbar.blockquote).removeClass('has-text-link')

    if (editor.isActive('link')) {
      $(topbar.link).addClass('has-text-link')
    } else $(topbar.link).removeClass('has-text-link')

    if (editor.isActive('codeBlock')) {
      $(topbar.code).addClass('has-text-link')
    } else $(topbar.code).removeClass('has-text-link')

    if (editor.isActive('subscript')) {
      $(topbar.subscript).addClass('has-text-link')
    } else $(topbar.subscript).removeClass('has-text-link')

    if (editor.isActive('superscript')) {
      $(topbar.superscript).addClass('has-text-link')
    } else $(topbar.superscript).removeClass('has-text-link')
  }

  editor.on('selectionUpdate', fn)
  editor.on('update', fn)
}
