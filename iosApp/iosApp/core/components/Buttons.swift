import SwiftUI

// MARK: - Design System — Buttons

// MARK: Primary Button
struct PrimaryButton: View {
    let title: String
    var isLoading: Bool = false
    var isDisabled: Bool = false
    let action: () -> Void

    init(_ title: String, isLoading: Bool = false, isDisabled: Bool = false, action: @escaping () -> Void) {
        self.title = title
        self.isLoading = isLoading
        self.isDisabled = isDisabled
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            Group {
                if isLoading {
                    ProgressView().tint(.white)
                } else {
                    Text(title)
                        .font(.dsLabelLarge)
                        .foregroundColor(.white)
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 52)
            .background(isDisabled ? Color.brandPrimary.opacity(0.4) : Color.brandPrimary)
            .cornerRadius(12)
        }
        .disabled(isLoading || isDisabled)
    }
}

// MARK: Secondary Button
struct SecondaryButton: View {
    let title: String
    let action: () -> Void

    init(_ title: String, action: @escaping () -> Void) {
        self.title = title
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.dsLabelLarge)
                .foregroundColor(.primary)
                .frame(maxWidth: .infinity)
                .frame(height: 52)
                .background(Color(.systemGray6))
                .cornerRadius(12)
        }
    }
}

// MARK: Destructive Text Button
struct DestructiveTextButton: View {
    let title: String
    let action: () -> Void

    init(_ title: String, action: @escaping () -> Void) {
        self.title = title
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.dsBodyMedium)
                .foregroundColor(.errorRed)
        }
    }
}

// MARK: Text Link Button (inline link)
struct TextLinkButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.dsBodyMedium)
                .foregroundColor(.brandPrimary)
                .underline()
        }
    }
}
