import SwiftUI
import UIKit

// MARK: - Design System — Semantic Color Tokens
// Поддержка светлой и тёмной темы через UIColor dynamic provider.
extension Color {

    // MARK: - Helper
    private static func adaptive(light: String, dark: String) -> Color {
        Color(uiColor: UIColor { trait in
            UIColor(hex: trait.userInterfaceStyle == .dark ? dark : light)
        })
    }

    // MARK: - Brand
    /// Основной акцентный цвет приложения
    static let brandPrimary        = adaptive(light: "4B5EFC", dark: "7B8FFF")
    /// Вариант основного цвета (нажатое состояние)
    static let brandPrimaryVariant = adaptive(light: "3A4DE0", dark: "6070EE")
    /// Лёгкий фон бренда (бейджи, аватары)
    static let brandBackground     = adaptive(light: "E8EAFF", dark: "1C2260")

    // MARK: - Surfaces
    /// Цвет фона страниц/экранов
    static let surfaceBackground   = Color(.systemGroupedBackground)
    /// Цвет поверхности карточки
    static let surfaceCard         = Color(.systemBackground)

    // MARK: - Text
    /// Вторичный текст / плейсхолдеры
    static let onSurfaceVariant    = Color(.secondaryLabel)

    // MARK: - Borders
    /// Цвет разделителей и обводок
    static let outline             = Color(.separator)

    // MARK: - NDA Badge
    static let ndaRequiredBg       = adaptive(light: "FFF3CD", dark: "3D2E00")
    static let ndaRequiredText     = adaptive(light: "92650A", dark: "FFD166")
    static let ndaNotRequiredBg    = Color(.systemGray6)
    static let ndaNotRequiredText  = Color(.secondaryLabel)

    // MARK: - Status Badges
    static let statusPendingBg     = adaptive(light: "FFF3CD", dark: "3D2E00")
    static let statusPendingText   = adaptive(light: "856404", dark: "FFD166")
    static let statusAcceptedBg    = adaptive(light: "D4EDDA", dark: "0A3D1E")
    static let statusAcceptedText  = adaptive(light: "155724", dark: "6EE7A0")
    static let statusRejectedBg    = adaptive(light: "F8D7DA", dark: "3D0A0A")
    static let statusRejectedText  = adaptive(light: "721C24", dark: "FF8080")

    // MARK: - Semantic success / warning / error
    static let successGreen        = adaptive(light: "28A745", dark: "4CD964")
    static let warningAmber        = adaptive(light: "FFC107", dark: "FFD60A")
    static let errorRed            = Color(.systemRed)
}
