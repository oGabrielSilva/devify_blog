import cpp from 'highlight.js/lib/languages/cpp'
import css from 'highlight.js/lib/languages/css'
import dart from 'highlight.js/lib/languages/dart'
import go from 'highlight.js/lib/languages/go'
import java from 'highlight.js/lib/languages/java'
import javascript from 'highlight.js/lib/languages/javascript'
import json from 'highlight.js/lib/languages/json'
import kotlin from 'highlight.js/lib/languages/kotlin'
import php from 'highlight.js/lib/languages/php'
import python from 'highlight.js/lib/languages/python'
import typescript from 'highlight.js/lib/languages/typescript'
import html from 'highlight.js/lib/languages/xml'

import hljs from 'highlight.js'

export function requireHLJS() {
  hljs.registerLanguage('javascript', javascript)
  hljs.registerLanguage('cpp', cpp)
  hljs.registerLanguage('css', css)
  hljs.registerLanguage('dart', dart)
  hljs.registerLanguage('go', go)
  hljs.registerLanguage('java', java)
  hljs.registerLanguage('json', json)
  hljs.registerLanguage('kotlin', kotlin)
  hljs.registerLanguage('php', php)
  hljs.registerLanguage('python', python)
  hljs.registerLanguage('typescript', typescript)
  hljs.registerLanguage('html', html)

  return hljs
}
