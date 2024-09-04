const esbuild = require('esbuild')
const { resolve } = require('path')
const fs = require('fs')

function cssInJsPlugin() {
  return {
    name: 'css-in-js',
    setup(build) {
      build.onResolve({ filter: /\.css$/ }, (args) => {
        return {
          path: resolve(args.resolveDir, args.path),
          namespace: 'css-in-js',
        }
      })

      build.onLoad({ filter: /.*/, namespace: 'css-in-js' }, async (args) => {
        const cssContent = await fs.promises.readFile(args.path, 'utf8')
        const contents = `
                  const style = document.createElement('style');
                  style.textContent = ${JSON.stringify(cssContent)};
                  document.head.appendChild(style);
                `
        return { contents, loader: 'js' }
      })
    },
  }
}

;(async () => {
  const isDev = process.argv && process.argv.includes('--dev')

  const options = {
    entryPoints: [
      resolve(__dirname, 'index.ts'),
      resolve(__dirname, 'index.internal.ts'),
      resolve(__dirname, 'index.editor.ts'),
    ],
    bundle: true,
    minify: true,
    sourcemap: isDev ? 'inline' : false,
    target: ['es6'],
    // plugins: [cssInJsPlugin()],

    outdir: resolve(__dirname, '..', 'static', 'javascript'),
  }
  if (!isDev) {
    await esbuild.build(options)
    console.log('Esbuild instruction completed')
    return
  }

  const context = await esbuild.context(options)
  console.log('Esbuild watch dev mode')

  await context.watch()
})()
