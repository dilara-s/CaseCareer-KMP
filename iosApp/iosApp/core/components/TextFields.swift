import SwiftUI

// MARK: - Design System — Text Fields

// MARK: Auth / Single-line TextField
struct AppTextField: View {
    let placeholder: String
    @Binding var text: String
    var error: String? = nil
    var hint: String? = nil
    var keyboardType: UIKeyboardType = .default
    var leadingIcon: String? = nil
    var isPassword: Bool = false

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack(spacing: 10) {
                if let icon = leadingIcon {
                    Image(systemName: icon)
                        .font(.system(size: 15))
                        .foregroundColor(.onSurfaceVariant)
                }
                Group {
                    if isPassword {
                        SecureField(placeholder, text: $text)
                    } else {
                        TextField(placeholder, text: $text)
                            .keyboardType(keyboardType)
                    }
                }
                .autocapitalization(.none)
                .autocorrectionDisabled()
            }
            .padding()
            .background(Color(.systemBackground))
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(
                        error != nil ? Color.errorRed : Color.outline,
                        lineWidth: 1
                    )
            )

            if let hint = hint, error == nil {
                Text(hint)
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
                    .padding(.horizontal, 4)
            }

            if let error = error {
                Text(error)
                    .font(.dsCaption)
                    .foregroundColor(.errorRed)
                    .padding(.horizontal, 4)
            }
        }
    }
}

// MARK: Legacy alias (чтобы не ломать AuthView, который использует AuthTextField)
typealias AuthTextField = AppTextField

// MARK: Multi-line TextArea
struct AppTextArea: View {
    let placeholder: String
    @Binding var text: String
    var error: String? = nil
    var minHeight: CGFloat = 120

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            ZStack(alignment: .topLeading) {
                TextEditor(text: $text)
                    .frame(minHeight: minHeight)
                    .padding(8)
                    .scrollContentBackground(.hidden)

                if text.isEmpty {
                    Text(placeholder)
                        .font(.dsBodySmall)
                        .foregroundColor(.onSurfaceVariant)
                        .padding(.horizontal, 13)
                        .padding(.vertical, 15)
                        .allowsHitTesting(false)
                }
            }
            .frame(minHeight: minHeight + 20)
            .background(Color(.systemBackground))
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(
                        error != nil ? Color.errorRed : Color.outline,
                        lineWidth: 1
                    )
            )

            if let error = error {
                Text(error)
                    .font(.dsCaption)
                    .foregroundColor(.errorRed)
                    .padding(.horizontal, 4)
            }
        }
    }
}

// MARK: ClickableText (навигационные ссылки типа «Уже есть аккаунт? Войти»)
struct ClickableText: View {
    let primary: String
    let action: String
    let onClick: () -> Void

    var body: some View {
        HStack(spacing: 0) {
            Text(primary)
                .foregroundColor(.onSurfaceVariant)
            Button(action) {
                onClick()
            }
            .foregroundColor(.brandPrimary)
            .fontWeight(.semibold)
        }
        .font(.dsBodySmall)
    }
}

// MARK: DS Checkbox
struct DSCheckbox: View {
    let label: String
    let isChecked: Bool
    var error: Bool = false
    let onToggle: () -> Void

    var body: some View {
        Button(action: onToggle) {
            HStack(alignment: .top, spacing: 12) {
                ZStack {
                    RoundedRectangle(cornerRadius: 5)
                        .stroke(
                            error ? Color.errorRed : Color.brandPrimary,
                            lineWidth: 2
                        )
                        .frame(width: 22, height: 22)
                    if isChecked {
                        Image(systemName: "checkmark")
                            .font(.system(size: 13, weight: .bold))
                            .foregroundColor(.brandPrimary)
                    }
                }
                Text(label)
                    .font(.dsBodySmall)
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .buttonStyle(.plain)
    }
}
