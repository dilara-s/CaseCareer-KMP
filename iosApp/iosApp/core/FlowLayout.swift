import SwiftUI

struct FlowLayout<Item: Hashable, Content: View>: View {
    let items: [Item]
    let content: (Item) -> Content

    @State private var totalHeight: CGFloat = .zero

    var body: some View {
        GeometryReader { geometry in
            self.generateContent(in: geometry)
        }
        .frame(height: totalHeight)
    }

    private func generateContent(in geometry: GeometryProxy) -> some View {
        var width: CGFloat = 0
        var height: CGFloat = 0
        var lastHeight: CGFloat = 0

        return ZStack(alignment: .topLeading) {
            ForEach(items, id: \.self) { item in
                content(item)
                    .alignmentGuide(.leading) { d in
                        if abs(width - d.width) > geometry.size.width {
                            width = 0
                            height -= lastHeight + 8
                        }
                        lastHeight = d.height
                        let result = width
                        if item == items.last {
                            width = 0
                        } else {
                            width -= d.width + 8
                        }
                        return result
                    }
                    .alignmentGuide(.top) { _ in
                        let result = height
                        if item == items.last {
                            height = 0
                        }
                        return result
                    }
            }
        }
        .background(viewHeightReader($totalHeight))
    }

    private func viewHeightReader(_ binding: Binding<CGFloat>) -> some View {
        GeometryReader { geometry in
            Color.clear
                .preference(key: HeightPreferenceKey.self, value: geometry.size.height)
        }
        .onPreferenceChange(HeightPreferenceKey.self) { binding.wrappedValue = $0 }
    }
}

private struct HeightPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = max(value, nextValue())
    }
}
