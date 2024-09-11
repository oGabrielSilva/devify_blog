import { ValidationKassiopeiaTool } from 'kassiopeia-tools'
import { dangerElement } from '../../utils/dangerElement'
import { BaseHandler } from '../BaseHandler'

export class AdminNewUserPageHandler extends BaseHandler {
  private readonly validation = new ValidationKassiopeiaTool()

  private readonly form = $<HTMLFormElement>('#new-user')
  private readonly name = $<HTMLInputElement>('#name')
  private readonly username = $<HTMLInputElement>('#username')
  private readonly email = $<HTMLInputElement>('#email')
  private readonly password = $<HTMLInputElement>('#password')
  private readonly passwordAgain = $<HTMLInputElement>('#password-again')

  private isNameValid = false
  private isUsernameValid = false
  private isEmailValid = false
  private isPasswordValid = false
  private isPasswordAgainValid = false

  private configureForm() {
    this.form.on('submit', (e) => {
      e.preventDefault()

      const { isEmailValid, isNameValid, isPasswordValid, isPasswordAgainValid, isUsernameValid } = this

      if (!isNameValid) {
        return dangerElement(this.name[0].parentElement!, 'O nome do usuário tem até 50 caracteres')
      }

      const name = this.name.val()!
      if (name.trim().length <= 0) {
        return dangerElement(this.name[0].parentElement!, 'Informe um nome')
      }

      if (!isUsernameValid) {
        return dangerElement(this.username[0].parentElement!, 'O username do usuário tem até 30 caracteres')
      }

      const username = this.username.val()!
      if (username.trim().length <= 0) {
        return dangerElement(this.username[0].parentElement!, 'Informe um username')
      }

      if (!isEmailValid) {
        return dangerElement(this.email[0].parentElement!, 'Informe um email válido')
      }

      if (!isPasswordValid) {
        return dangerElement(this.password[0].parentElement!, 'Informe uma senha válida')
      }

      if (!isPasswordAgainValid) {
        anim
          .shakeX(this.password[0].parentElement!, false)
          .addEventOnCompletion(() => anim.clean(this.password[0].parentElement!))
        return dangerElement(this.passwordAgain[0].parentElement!, 'As senhas não são iguais')
      }

      locker.lock()
      this.form[0].submit()
    })
  }

  private validateName() {
    this.isNameValid = this.name.val()!.length <= 50

    if (this.isNameValid) {
      this.name.removeClass('is-danger')
    } else {
      this.name.addClass('is-danger')
    }
  }

  private validateUsername() {
    this.isUsernameValid = this.username.val()!.length <= 30

    if (this.isUsernameValid) {
      this.username.removeClass('is-danger')
    } else {
      this.username.addClass('is-danger')
    }
  }

  private validateEmail() {
    const email = this.email.val()!
    this.isEmailValid = this.validation.isEmailValid(email) && email.length <= 150

    if (email.trim().length === 0) return

    if (this.isEmailValid) {
      this.email.removeClass('is-danger')
    } else {
      this.email.addClass('is-danger')
    }
  }

  private validatePassword() {
    const pass = this.password.val()!

    this.isPasswordValid = pass.length >= 8

    if (pass.trim().length === 0) return
    this.validatePasswordAgain()

    if (this.isPasswordValid) {
      this.password.removeClass('is-danger')
    } else {
      this.password.addClass('is-danger')
    }
  }

  private validatePasswordAgain() {
    const pass = this.passwordAgain.val()!

    this.isPasswordAgainValid = this.isPasswordValid && pass === this.password.val()

    if (this.isPasswordAgainValid) {
      this.passwordAgain.removeClass('is-danger')
    } else {
      this.passwordAgain.addClass('is-danger')
    }
  }

  public handle(): void {
    this.configureForm()

    this.validateName()
    this.validateUsername()
    this.validateEmail()
    this.validatePassword()

    this.name.on('input', () => this.validateName())
    this.username.on('input', () => {
      const val = this.username.val()!
      this.username.val(this.validation.slugify(val))

      this.validateUsername()
    })
    this.email.on('input', () => this.validateEmail())
    this.password.on('input', () => this.validatePassword())
    this.passwordAgain.on('input', () => this.validatePasswordAgain())
  }
}
