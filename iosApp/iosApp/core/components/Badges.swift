import SwiftUI

// MARK: - Design System — Badges

// MARK: NDA Badge
struct NdaBadge: View {
    let ndaRequired: Bool

    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: ndaRequired ? "lock.fill" : "lock.open.fill")
                .font(.system(size: 11, weight: .medium))
            Text(ndaRequired ? "NDA требуется" : "NDA не требуется")
                .font(.dsLabelSmall)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 5)
        .background(ndaRequired ? Color.ndaRequiredBg : Color.ndaNotRequiredBg)
        .foregroundColor(ndaRequired ? Color.ndaRequiredText : Color.ndaNotRequiredText)
        .cornerRadius(20)
    }
}

// MARK: Status Badge (отклик: на проверке / принято / отклонено)
struct StatusBadge: View {
    let status: String

    private var colors: (bg: Color, text: Color) {
        switch status.lowercased() {
        case "sent", "отправлен":
            return (.brandBackground, .brandPrimary)
        case "на проверке", "pending", "in_review", "review":
            return (.statusPendingBg, .statusPendingText)
        case "принято", "accepted", "approved":
            return (.statusAcceptedBg, .statusAcceptedText)
        case "отклонено", "rejected":
            return (.statusRejectedBg, .statusRejectedText)
        default:
            return (.brandBackground, .brandPrimary)
        }
    }

    /// Локализованное отображение статуса
    private var displayText: String {
        switch status.lowercased() {
        case "sent":                            return "Отправлен"
        case "pending", "in_review", "review":  return "На проверке"
        case "accepted", "approved":            return "Принято"
        case "rejected":                        return "Отклонено"
        default:                                return status
        }
    }

    var body: some View {
        Text(displayText)
            .font(.dsLabelSmall)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(colors.bg)
            .foregroundColor(colors.text)
            .cornerRadius(20)
    }
}

// MARK: Role Badge (Студент / Компания)
struct RoleBadge: View {
    let role: String

    private var displayText: String {
        switch role.uppercased() {
        case "STUDENT": return "Студент"
        case "COMPANY": return "Компания"
        default:        return role
        }
    }

    var body: some View {
        Text(displayText)
            .font(.dsLabelSmall)
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(Color.brandBackground)
            .foregroundColor(Color.brandPrimary)
            .cornerRadius(20)
    }
}
