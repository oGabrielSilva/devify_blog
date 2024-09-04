export type Lang = 'pt-BR' | 'en'

export const supportedLangs: Array<Lang> = ['pt-BR', 'en']

export function formatDate(date: Date, local: Lang = 'pt-BR') {
  return date.toLocaleDateString(local, {
    hour: '2-digit',
    hour12: false,
    minute: '2-digit',
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    weekday: 'narrow',
    timeZoneName: 'short',
    formatMatcher: 'basic',
  })
}
