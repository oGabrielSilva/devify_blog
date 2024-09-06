import { type Editor } from '@tiptap/core'
import { generateHTML } from 'kassiopeia-tools'

export type Topbar = ReturnType<typeof generateTopbar>

export function generateTopbar(editor: Editor) {
  const bold = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleBold().run()
    },
    attributes: {
      title: 'Bold [Ctrl + B]',
      type: 'button',
      'aria-label': 'Aplica negrito no texto selecionado',
      class: 'bold button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-bold',
            },
          },
        ],
      },
    ],
  })

  const italic = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleItalic().run()
    },
    attributes: {
      title: 'Itálico [Ctrl + i]',
      type: 'button',
      'aria-label': 'Aplica itálico no texto selecionado',
      class: 'italic button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-italic',
            },
          },
        ],
      },
    ],
  })

  const underline = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleUnderline().run()
    },
    attributes: {
      title: 'Sublinhado [Ctrl + U]',
      type: 'button',
      'aria-label': 'Aplica sublinhar texto selecionado',
      class: 'underline button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-underline',
            },
          },
        ],
      },
    ],
  })

  const strike = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleStrike().run()
    },
    attributes: {
      title: 'Tachar [Ctrl + Shift + S]',
      type: 'button',
      'aria-label': 'Tachar texto selecionado',
      class: 'strike button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-strikethrough',
            },
          },
        ],
      },
    ],
  })

  const h1 = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleHeading({ level: 1 }).run()
    },
    attributes: {
      title: 'Título [Ctrl + Alt + 1]',
      type: 'button',
      'aria-label': 'Define texto como título',
      class: 'h1 button is-text px-2 mr-1',
    },
    css: {
      textDecoration: 'none',
      fontFamily: 'JetBrains Mono',
    },
    children: [
      {
        tag: 'span',
        textContent: 'H',
        children: [
          {
            tag: 'sub',
            textContent: '1',
          },
        ],
      },
    ],
  })
  const h2 = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleHeading({ level: 2 }).run()
    },
    attributes: {
      title: 'Subtítulo [Ctrl + Alt + 2]',
      type: 'button',
      'aria-label': 'Define texto como subtítulo',
      class: 'h2 button is-text px-2',
    },
    css: {
      textDecoration: 'none',
      fontFamily: 'JetBrains Mono',
    },
    children: [
      {
        tag: 'span',
        textContent: 'H',
        children: [
          {
            tag: 'sub',
            textContent: '2',
          },
        ],
      },
    ],
  })

  const ol = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleOrderedList().run()
    },
    attributes: {
      title: 'Lista numerada [Ctrl + Shift + 7]',
      type: 'button',
      'aria-label': 'Defina uma lista numerada',
      class: 'ordenedList button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-list-ol',
            },
          },
        ],
      },
    ],
  })

  const ul = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleBulletList().run()
    },
    attributes: {
      title: 'Lista com marcadores [Ctrl + Shift + 8]',
      type: 'button',
      'aria-label': 'Defina uma lista com marcadores',
      class: 'bulletList button is-text',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-list-ul',
            },
          },
        ],
      },
    ],
  })

  const colorPicker = generateHTML({
    tag: 'button',
    attributes: {
      title: 'Cor do texto',
      type: 'button',
      'aria-label': 'Defina uma cor para o texto',
      class: 'colorPicker button is-text p-2',
    },
    css: { position: 'relative' },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small m-0 p-0',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-font',
            },
          },
        ],
      },
      {
        tag: 'input',
        attributes: { class: 'pick', type: 'color' },
        css: {
          position: 'absolute',
          bottom: '10%',
          height: '4px',
          background: 'transparent',
          border: 'none',
          boxShadow: 'none',
          outline: 'none',
          width: '100%',
        },
      },
    ],
  })
  colorPicker.onclick = () => colorPicker.querySelector<HTMLElement>('.pick')?.click()
  colorPicker.querySelector<HTMLInputElement>('.pick')!.oninput = (e) => {
    editor
      .chain()
      .focus()
      .setColor((e.currentTarget as HTMLInputElement).value)
      .run()
  }

  const bgPicker = generateHTML({
    tag: 'button',
    attributes: {
      title: 'Cor de fundo do texto',
      type: 'button',
      'aria-label': 'Defina uma cor de fundo para o texto',
      class: 'bgPicker button is-text p-2',
    },
    css: { position: 'relative' },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small m-0 p-0',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-highlighter',
            },
          },
        ],
      },
      {
        tag: 'input',
        attributes: { class: 'pick', type: 'color' },
        css: {
          position: 'absolute',
          bottom: '10%',
          height: '4px',
          background: 'transparent',
          border: 'none',
          boxShadow: 'none',
          outline: 'none',
          width: '100%',
        },
      },
    ],
  })
  bgPicker.onclick = () => bgPicker.querySelector<HTMLElement>('.pick')?.click()
  bgPicker.querySelector<HTMLInputElement>('.pick')!.oninput = (e) => {
    editor
      .chain()
      .focus()
      .setHighlight({ color: (e.currentTarget as HTMLInputElement).value })
      .run()
  }

  const clean = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().unsetColor().unsetHighlight().run()
    },
    attributes: {
      title: 'Remover cor e destaque do texto',
      type: 'button',
      class: 'clearColorAndHighLight button is-text',
      'aria-label': 'Use para remover a cor e o destaque do texto',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-eraser',
            },
          },
        ],
      },
    ],
  })

  const alignLeft = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().setTextAlign('left').run()
    },
    attributes: {
      title: 'Alinhar à esquerda [Ctrl + Shift + L]',
      type: 'button',
      class: 'alignLeft button is-text',
      'aria-label': 'Alinhar texto à esquerda',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-align-left',
            },
          },
        ],
      },
    ],
  })

  const alignCenter = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().setTextAlign('center').run()
    },
    attributes: {
      title: 'Alinhar ao centro [Ctrl + Shift + E]',
      type: 'button',
      class: 'alignCenter button is-text',
      'aria-label': 'Alinhar texto ao centro',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-align-center',
            },
          },
        ],
      },
    ],
  })

  const alignRight = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().setTextAlign('right').run()
    },
    attributes: {
      title: 'Alinhar à direita [Ctrl + Shift + R]',
      type: 'button',
      class: 'alignRight button is-text',
      'aria-label': 'Alinhar texto à direita',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-align-right',
            },
          },
        ],
      },
    ],
  })

  const alignJustify = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().setTextAlign('justify').run()
    },
    attributes: {
      title: 'Justificar [Ctrl + Shift + J]',
      type: 'button',
      class: 'alignJustify button is-text',
      'aria-label': 'Justificar texto',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-align-justify',
            },
          },
        ],
      },
    ],
  })

  const indent = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().indent().run()
    },
    attributes: {
      title: 'Aumentar recuo [Tab]',
      type: 'button',
      class: 'indent button is-text',
      'aria-label': 'Aumentar recuo do texto',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-indent',
            },
          },
        ],
      },
    ],
  })

  const outdent = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().outdent().run()
    },
    attributes: {
      title: 'Diminuir recuo [Shift + Tab]',
      type: 'button',
      class: 'outdent button is-text',
      'aria-label': 'Diminuir recuo do texto',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-outdent',
            },
          },
        ],
      },
    ],
  })

  const blockquote = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleBlockquote().run()
    },
    attributes: {
      title: 'Citação [Ctrl + Shift + B]',
      type: 'button',
      class: 'blockquote button is-text',
      'aria-label': 'Transforma o texto em citação',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-quote-left',
            },
          },
        ],
      },
    ],
  })

  const link = generateHTML({
    tag: 'button',
    onClick: () => {
      const previousUrl = editor.getAttributes('link').href
      const url = window.prompt('URL', previousUrl)

      if (url === null) {
        return
      }

      if (url === '') {
        editor.chain().focus().extendMarkRange('link').unsetLink().run()

        return
      }

      editor.chain().focus().extendMarkRange('link').setLink({ href: url }).run()
    },
    attributes: {
      title: 'Adicione um link',
      type: 'button',
      class: 'link button is-text',
      'aria-label': 'Transforme o texto em um link',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-link',
            },
          },
        ],
      },
    ],
  })

  const code = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleCodeBlock().run()
    },
    attributes: {
      title: 'Bloco de código [Ctrl + Alt + C]',
      type: 'button',
      class: 'code button is-text',
      'aria-label': 'Criar um bloco de código',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-code',
            },
          },
        ],
      },
    ],
  })

  const subscript = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleSubscript().run()
    },
    attributes: {
      title: 'Subscrito [Ctrl + ,]',
      type: 'button',
      class: 'subscript button is-text',
      'aria-label': 'Transformar texto em subscrito',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-subscript',
            },
          },
        ],
      },
    ],
  })

  const superscript = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().toggleSuperscript().run()
    },
    attributes: {
      title: 'Sobrescrito [Ctrl + .]',
      type: 'button',
      class: 'superscript button is-text',
      'aria-label': 'Transformar texto selecionado em sobrescrito',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-superscript',
            },
          },
        ],
      },
    ],
  })

  const undo = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().undo().run()
    },
    attributes: {
      title: 'Desfazer [Ctrl + Z]',
      type: 'button',
      class: 'undo button is-text',
      'aria-label': 'Desfazer última ação',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-rotate-left',
            },
          },
        ],
      },
    ],
  })

  const redo = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.chain().focus().redo().run()
    },
    attributes: {
      title: 'Refazer [Ctrl + Y]',
      type: 'button',
      class: 'redo button is-text',
      'aria-label': 'Refazer última ação',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-rotate-right',
            },
          },
        ],
      },
    ],
  })

  const unsetAllMarks = generateHTML({
    tag: 'button',
    onClick: () => {
      editor.commands.unsetAllMarks()
    },
    attributes: {
      title: 'Remover todas as marcações',
      type: 'button',
      class: 'unsetAllMarks button is-text',
      'aria-label': 'Remover todas as marcações do texto selecionado',
    },
    children: [
      {
        tag: 'span',
        attributes: {
          class: 'icon is-small',
        },
        children: [
          {
            tag: 'i',
            attributes: {
              class: 'fa-solid fa-text-slash',
            },
          },
        ],
      },
    ],
  })

  const container = generateHTML({
    tag: 'div',
    attributes: {
      class: 'buttons gap',
    },
  })

  container.append(
    bold,
    italic,
    underline,
    strike,
    h1,
    h2,
    ol,
    ul,
    colorPicker,
    bgPicker,
    clean,
    alignLeft,
    alignCenter,
    alignRight,
    alignJustify,
    indent,
    outdent,
    blockquote,
    link,
    code,
    subscript,
    superscript,
    undo,
    redo,
    unsetAllMarks
  )

  return {
    container,
    bold,
    italic,
    underline,
    strike,
    h1,
    h2,
    ol,
    ul,
    colorPicker,
    bgPicker,
    clean,
    alignLeft,
    alignRight,
    alignCenter,
    alignJustify,
    indent,
    outdent,
    blockquote,
    link,
    code,
    subscript,
    superscript,
    undo,
    redo,
    unsetAllMarks,
  }
}
