const fs = require('fs')
const xml2js = require('xml2js')
const { exec } = require('child_process')
const readline = require('readline')

// Função para ler a versão atual do pom.xml
async function readPomVersion(pomPath) {
  const xml = fs.readFileSync(pomPath, 'utf-8')
  const parser = new xml2js.Parser()
  const result = await parser.parseStringPromise(xml)
  return result.project.version[0]
}

// Função para atualizar a versão no pom.xml
async function updatePomVersion(pomPath, newVersion) {
  const xml = fs.readFileSync(pomPath, 'utf-8')
  const parser = new xml2js.Parser()
  const result = await parser.parseStringPromise(xml)

  // Atualiza a versão
  result.project.version[0] = newVersion

  // Converte o objeto de volta para XML
  const builder = new xml2js.Builder()
  const updatedXml = builder.buildObject(result)

  // Grava o novo XML no arquivo pom.xml
  fs.writeFileSync(pomPath, updatedXml)
  console.log(`Versão atualizada para ${newVersion} no arquivo pom.xml.`)
}

// Função para fazer commit no GitHub
async function commitToGit(version) {
  exec(
    'git add . && git commit -m "Release v:' + version + '" && git push origin main',
    (error, stdout, stderr) => {
      if (error) {
        console.error(`Erro ao executar git: ${error.message}`)
        return
      }
      if (stderr) {
        console.error(`Erro no git: ${stderr}`)
        return
      }
      console.log(`Git push feito: ${stdout}`)
    }
  )
}

// Função para rodar o Maven e gerar o JAR
async function buildMaven() {
  exec('mvn clean package -DskipTests', (error, stdout, stderr) => {
    if (error) {
      console.error(`Erro ao executar Maven: ${error.message}`)
      return
    }
    if (stderr) {
      console.error(`Erro no Maven: ${stderr}`)
      return
    }
    console.log(`Maven build concluído: ${stdout}`)
  })
}

// Função principal
async function main() {
  const pomPath = 'pom.xml'

  // Lê a versão atual do pom.xml
  const currentVersion = await readPomVersion(pomPath)
  console.log(`Versão atual: ${currentVersion}`)

  // Configura o readline para capturar a entrada do usuário
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  })

  // Pergunta a nova versão
  rl.question('Qual a nova versão? ', async (newVersion) => {
    newVersion = newVersion.trim() || currentVersion // Caso o usuário não insira uma nova versão, mantém a versão atual

    // Atualiza a versão no pom.xml
    await updatePomVersion(pomPath, newVersion)

    // Faz commit e push no GitHub
    await commitToGit(newVersion)

    // Roda o Maven para gerar o JAR
    await buildMaven()

    rl.close()
  })
}

main()
