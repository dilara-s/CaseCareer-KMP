import SwiftUI

// MARK: - Design System — Typography
// Единая типографика для всего iOS-приложения.
// Использует Dynamic Type — размеры масштабируются с настройками доступности.

extension Font {

    // MARK: - Display (экранные заголовки)
    /// Крупный заголовок экрана, напр. «Войти»
    static let dsDisplayLarge  = Font.largeTitle.weight(.bold)
    /// Заголовок раздела, напр. «Регистрация»
    static let dsDisplayMedium = Font.title.weight(.bold)
    /// Подзаголовок экрана, напр. «Отклик на кейс»
    static let dsDisplaySmall  = Font.title2.weight(.bold)

    // MARK: - Headline (заголовки секций)
    static let dsHeadlineLarge  = Font.title3.weight(.semibold)
    static let dsHeadlineMedium = Font.headline.weight(.semibold)
    static let dsHeadlineSmall  = Font.subheadline.weight(.semibold)

    // MARK: - Body (основной текст)
    static let dsBodyLarge   = Font.body
    static let dsBodyMedium  = Font.callout
    static let dsBodySmall   = Font.subheadline

    // MARK: - Label (метки, кнопки)
    static let dsLabelLarge  = Font.body.weight(.semibold)
    static let dsLabelMedium = Font.subheadline.weight(.medium)
    static let dsLabelSmall  = Font.caption.weight(.medium)

    // MARK: - Caption (подписи, вспомогательный текст)
    static let dsCaption      = Font.caption
    static let dsCaptionSmall = Font.caption2
}

// MARK: - Text Style Modifiers
extension View {
    /// Стиль для поля-метки (например заголовок TextField)
    func dsFieldLabel() -> some View {
        self.font(.dsLabelMedium).foregroundColor(.onSurfaceVariant)
    }

    /// Стиль для секционного заголовка (UPPERCASE caption)
    func dsSectionTitle() -> some View {
        self.font(.dsCaptionSmall.uppercaseSmallCaps()).foregroundColor(.onSurfaceVariant)
    }
}
