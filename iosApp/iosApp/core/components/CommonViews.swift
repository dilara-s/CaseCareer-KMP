import SwiftUI

// MARK: - Design System — Common Reusable Views

// MARK: Empty State
struct EmptyStateView: View {
    let icon: String
    let title: String
    let subtitle: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: icon)
                .font(.system(size: 52))
                .foregroundColor(.onSurfaceVariant)
            VStack(spacing: 6) {
                Text(title)
                    .font(.dsHeadlineMedium)
                Text(subtitle)
                    .font(.dsBodySmall)
                    .foregroundColor(.onSurfaceVariant)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
    }
}

// MARK: Error State
struct ErrorStateView: View {
    let message: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 44))
                .foregroundColor(.warningAmber)

            VStack(spacing: 8) {
                Text("Не удалось загрузить данные")
                    .font(.dsHeadlineMedium)
                Text(message)
                    .font(.dsBodySmall)
                    .foregroundColor(.onSurfaceVariant)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }

            PrimaryButton("Повторить", action: onRetry)
                .padding(.horizontal, 48)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
    }
}

// MARK: Card Container
struct CardView<Content: View>: View {
    let content: () -> Content

    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }

    var body: some View {
        content()
            .padding()
            .background(Color.surfaceCard)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.07), radius: 6, x: 0, y: 2)
    }
}

// MARK: Section Label (капсом над секцией)
struct SectionLabel: View {
    let title: String

    var body: some View {
        Text(title)
            .dsSectionTitle()
    }
}

// MARK: Info Row (label: value)
struct InfoRow: View {
    let label: String
    let value: String

    var body: some View {
        HStack {
            Text(label)
                .font(.dsBodySmall)
                .foregroundColor(.onSurfaceVariant)
            Spacer()
            Text(value)
                .font(.dsBodySmall)
        }
    }
}

// MARK: Divider with padding
struct PaddedDivider: View {
    var body: some View {
        Divider()
            .padding(.vertical, 4)
    }
}

// MARK: Bullet Status Row (для экрана успеха отклика)
struct BulletStatusRow: View {
    let title: String
    let subtitle: String
    var color: Color = .brandPrimary

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            Circle()
                .fill(color)
                .frame(width: 8, height: 8)
                .padding(.top, 6)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.dsLabelMedium)
                Text(subtitle)
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
            }
            Spacer()
        }
    }
}
