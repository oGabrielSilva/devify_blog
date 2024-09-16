const { spawn } = require('child_process')
const path = require('path')

const tsPath = path.resolve(__dirname, 'src', 'main', 'resources', 'typescript')

spawn('yarn', ['dev'], { cwd: tsPath, stdio: 'inherit', shell: true })
