package com.rizwansayyed.zene.presenter.model

data class MusicHeaderAlbumResponse(
    val responseContext: ResponseContext?,
    val contents: Contents?,
    val header: Header?,
    val metadata: Metadata?,
    val trackingParams: String?,
    val topbar: Topbar?,
    val microformat: Microformat?,
    val frameworkUpdates: FrameworkUpdates?,
)

data class ResponseContext(
    val serviceTrackingParams: List<ServiceTrackingParam>?,
    val maxAgeSeconds: Long?,
    val mainAppWebResponseContext: MainAppWebResponseContext?,
    val webResponseContextExtensionData: WebResponseContextExtensionData?,
)

data class ServiceTrackingParam(
    val service: String?,
    val params: List<Param>?,
)

data class Param(
    val key: String?,
    val value: String?,
)

data class MainAppWebResponseContext(
    val loggedOut: Boolean?,
    val trackingParam: String?,
)

data class WebResponseContextExtensionData(
    val ytConfigData: YtConfigData?,
    val hasDecorated: Boolean?,
)

data class YtConfigData(
    val visitorData: String?,
    val rootVisualElementType: Long?,
)

data class Contents(
    val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?,
)

data class TwoColumnBrowseResultsRenderer(
    val tabs: List<Tab>?,
)

data class Tab(
    val tabRenderer: TabRenderer?,
)

data class TabRenderer(
    val endpoint: Endpoint?,
    val title: String?,
    val selected: Boolean?,
    val content: Content?,
    val trackingParams: String?,
)

data class Endpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata?,
    val browseEndpoint: BrowseEndpoint?,
)

data class CommandMetadata(
    val webCommandMetadata: WebCommandMetadata?,
)

data class WebCommandMetadata(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint(
    val browseId: String?,
    val params: String?,
    val canonicalBaseUrl: String?,
)

data class Content(
    val sectionListRenderer: SectionListRenderer?,
)

data class SectionListRenderer(
    val contents: List<Content2>?,
    val trackingParams: String?,
    val targetId: String?,
    val disablePullToRefresh: Boolean?,
)

data class Content2(
    val itemSectionRenderer: ItemSectionRenderer?,
)

data class ItemSectionRenderer(
    val contents: List<Content3>?,
    val trackingParams: String?,
)

data class Content3(
    val shelfRenderer: ShelfRenderer?,
)

data class ShelfRenderer(
    val title: Title?,
    val endpoint: Endpoint2?,
    val content: Content4?,
    val trackingParams: String?,
    val playAllButton: PlayAllButton?,
)

data class Title(
    val runs: List<Run>?,
)

data class Run(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint?,
)

data class NavigationEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata2?,
    val browseEndpoint: BrowseEndpoint2?,
)

data class CommandMetadata2(
    val webCommandMetadata: WebCommandMetadata2?,
)

data class WebCommandMetadata2(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint2(
    val params: String?,
    val canonicalBaseUrl: String?,
    val browseId: String?,
)

data class Endpoint2(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata3?,
    val browseEndpoint: BrowseEndpoint3?,
)

data class CommandMetadata3(
    val webCommandMetadata: WebCommandMetadata3?,
)

data class WebCommandMetadata3(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint3(
    val params: String?,
    val canonicalBaseUrl: String?,
    val browseId: String?,
)

data class Content4(
    val horizontalListRenderer: HorizontalListRenderer?,
)

data class HorizontalListRenderer(
    val items: List<Item>?,
    val trackingParams: String?,
    val visibleItemCount: Long?,
    val nextButton: NextButton?,
    val previousButton: PreviousButton?,
)

data class Item(
    val compactStationRenderer: CompactStationRenderer?,
    val gridPlaylistRenderer: GridPlaylistRenderer?,
    val gridVideoRenderer: GridVideoRenderer?,
)

data class CompactStationRenderer(
    val title: Title2?,
    val description: Description?,
    val videoCountText: VideoCountText?,
    val navigationEndpoint: NavigationEndpoint2?,
    val thumbnail: Thumbnail?,
    val trackingParams: String?,
)

data class Title2(
    val simpleText: String?,
)

data class Description(
    val simpleText: String?,
)

data class VideoCountText(
    val runs: List<Run2>?,
)

data class Run2(
    val text: String?,
)

data class NavigationEndpoint2(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata4?,
    val watchEndpoint: WatchEndpoint?,
)

data class CommandMetadata4(
    val webCommandMetadata: WebCommandMetadata4?,
)

data class WebCommandMetadata4(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint(
    val videoId: String?,
    val playlistId: String?,
    val params: String?,
    val continuePlayback: Boolean?,
    val loggingContext: LoggingContext?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?,
)

data class LoggingContext(
    val vssLoggingContext: VssLoggingContext?,
)

data class VssLoggingContext(
    val serializedContextData: String?,
)

data class WatchEndpointSupportedOnesieConfig(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?,
)

data class Html5PlaybackOnesieConfig(
    val commonConfig: CommonConfig?,
)

data class CommonConfig(
    val url: String?,
)

data class Thumbnail(
    val thumbnails: List<Thumbnail13>?,
)

data class GridPlaylistRenderer(
    val playlistId: String?,
    val thumbnail: Thumbnail3?,
    val title: Title3?,
    val shortBylineText: ShortBylineText?,
    val videoCountText: VideoCountText2?,
    val navigationEndpoint: NavigationEndpoint5?,
    val publishedTimeText: PublishedTimeText?,
    val videoCountShortText: VideoCountShortText?,
    val trackingParams: String?,
    val sidebarThumbnails: List<SidebarThumbnail>?,
    val thumbnailText: ThumbnailText?,
    val thumbnailRenderer: ThumbnailRenderer?,
    val longBylineText: LongBylineText?,
    val thumbnailOverlays: List<ThumbnailOverlay>?,
    val viewPlaylistText: ViewPlaylistText?,
)

data class Thumbnail3(
    val thumbnails: List<Thumbnail4>?,
    val sampledThumbnailColor: SampledThumbnailColor?,
    val darkColorPalette: DarkColorPalette?,
    val vibrantColorPalette: VibrantColorPalette?,
)

data class Thumbnail4(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class SampledThumbnailColor(
    val red: Long?,
    val green: Long?,
    val blue: Long?,
)

data class DarkColorPalette(
    val section2Color: Long?,
    val iconInactiveColor: Long?,
)

data class VibrantColorPalette(
    val iconInactiveColor: Long?,
)

data class Title3(
    val runs: List<Run3>?,
)

data class Run3(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint3?,
)

data class NavigationEndpoint3(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata5?,
    val watchEndpoint: WatchEndpoint2?,
)

data class CommandMetadata5(
    val webCommandMetadata: WebCommandMetadata5?,
)

data class WebCommandMetadata5(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint2(
    val videoId: String?,
    val playlistId: String?,
    val params: String?,
    val loggingContext: LoggingContext2?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig2?,
)

data class LoggingContext2(
    val vssLoggingContext: VssLoggingContext2?,
)

data class VssLoggingContext2(
    val serializedContextData: String?,
)

data class WatchEndpointSupportedOnesieConfig2(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig2?,
)

data class Html5PlaybackOnesieConfig2(
    val commonConfig: CommonConfig2?,
)

data class CommonConfig2(
    val url: String?,
)

data class ShortBylineText(
    val runs: List<Run4>?,
)

data class Run4(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint4?,
)

data class NavigationEndpoint4(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata6?,
    val browseEndpoint: BrowseEndpoint4?,
)

data class CommandMetadata6(
    val webCommandMetadata: WebCommandMetadata6?,
)

data class WebCommandMetadata6(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint4(
    val browseId: String?,
    val canonicalBaseUrl: String?,
)

data class VideoCountText2(
    val runs: List<Run5>?,
)

data class Run5(
    val text: String?,
)

data class NavigationEndpoint5(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata7?,
    val watchEndpoint: WatchEndpoint3?,
)

data class CommandMetadata7(
    val webCommandMetadata: WebCommandMetadata7?,
)

data class WebCommandMetadata7(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint3(
    val videoId: String?,
    val playlistId: String?,
    val params: String?,
    val loggingContext: LoggingContext3?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig3?,
)

data class LoggingContext3(
    val vssLoggingContext: VssLoggingContext3?,
)

data class VssLoggingContext3(
    val serializedContextData: String?,
)

data class WatchEndpointSupportedOnesieConfig3(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig3?,
)

data class Html5PlaybackOnesieConfig3(
    val commonConfig: CommonConfig3?,
)

data class CommonConfig3(
    val url: String?,
)

data class PublishedTimeText(
    val simpleText: String?,
)

data class VideoCountShortText(
    val simpleText: String?,
)

data class SidebarThumbnail(
    val thumbnails: List<Thumbnail5>?,
)

data class Thumbnail5(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class ThumbnailText(
    val runs: List<Run6>?,
)

data class Run6(
    val text: String?,
    val bold: Boolean?,
)

data class ThumbnailRenderer(
    val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?,
)

data class PlaylistVideoThumbnailRenderer(
    val thumbnail: Thumbnail6?,
    val trackingParams: String?,
)

data class Thumbnail6(
    val thumbnails: List<Thumbnail7>?,
    val sampledThumbnailColor: SampledThumbnailColor2?,
    val darkColorPalette: DarkColorPalette2?,
    val vibrantColorPalette: VibrantColorPalette2?,
)

data class Thumbnail7(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class SampledThumbnailColor2(
    val red: Long?,
    val green: Long?,
    val blue: Long?,
)

data class DarkColorPalette2(
    val section2Color: Long?,
    val iconInactiveColor: Long?,
)

data class VibrantColorPalette2(
    val iconInactiveColor: Long?,
)

data class LongBylineText(
    val runs: List<Run7>?,
)

data class Run7(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint6?,
)

data class NavigationEndpoint6(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata8?,
    val browseEndpoint: BrowseEndpoint5?,
)

data class CommandMetadata8(
    val webCommandMetadata: WebCommandMetadata8?,
)

data class WebCommandMetadata8(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint5(
    val browseId: String?,
    val canonicalBaseUrl: String?,
)

data class ThumbnailOverlay(
    val thumbnailOverlayBottomPanelRenderer: ThumbnailOverlayBottomPanelRenderer?,
    val thumbnailOverlayHoverTextRenderer: ThumbnailOverlayHoverTextRenderer?,
    val thumbnailOverlayNowPlayingRenderer: ThumbnailOverlayNowPlayingRenderer?,
)

data class ThumbnailOverlayBottomPanelRenderer(
    val text: Text?,
    val icon: Icon?,
)

data class Text(
    val simpleText: String?,
)

data class Icon(
    val iconType: String?,
)

data class ThumbnailOverlayHoverTextRenderer(
    val text: Text2?,
    val icon: Icon2?,
)

data class Text2(
    val runs: List<Run8>?,
)

data class Run8(
    val text: String?,
)

data class Icon2(
    val iconType: String?,
)

data class ThumbnailOverlayNowPlayingRenderer(
    val text: Text3?,
)

data class Text3(
    val runs: List<Run9>?,
)

data class Run9(
    val text: String?,
)

data class ViewPlaylistText(
    val runs: List<Run10>?,
)

data class Run10(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint7?,
)

data class NavigationEndpoint7(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata9?,
    val browseEndpoint: BrowseEndpoint6?,
)

data class CommandMetadata9(
    val webCommandMetadata: WebCommandMetadata9?,
)

data class WebCommandMetadata9(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint6(
    val browseId: String?,
)

data class GridVideoRenderer(
    val videoId: String?,
    val thumbnail: Thumbnail8?,
    val title: Title4?,
    val publishedTimeText: PublishedTimeText2?,
    val viewCountText: ViewCountText?,
    val navigationEndpoint: NavigationEndpoint8?,
    val shortBylineText: ShortBylineText2?,
    val ownerBadges: List<OwnerBadge>?,
    val trackingParams: String?,
    val offlineability: Offlineability?,
    val shortViewCountText: ShortViewCountText?,
    val menu: Menu?,
    val thumbnailOverlays: List<ThumbnailOverlay2>?,
    val badges: List<Badge>?,
    val richThumbnail: RichThumbnail?,
)

data class Thumbnail8(
    val thumbnails: List<Thumbnail9>?,
)

data class Thumbnail9(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Title4(
    val accessibility: Accessibility?,
    val simpleText: String?,
)

data class Accessibility(
    val accessibilityData: AccessibilityData?,
)

data class AccessibilityData(
    val label: String?,
)

data class PublishedTimeText2(
    val simpleText: String?,
)

data class ViewCountText(
    val simpleText: String?,
)

data class NavigationEndpoint8(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata10?,
    val watchEndpoint: WatchEndpoint4?,
)

data class CommandMetadata10(
    val webCommandMetadata: WebCommandMetadata10?,
)

data class WebCommandMetadata10(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint4(
    val videoId: String?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig4?,
)

data class WatchEndpointSupportedOnesieConfig4(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig4?,
)

data class Html5PlaybackOnesieConfig4(
    val commonConfig: CommonConfig4?,
)

data class CommonConfig4(
    val url: String?,
)

data class ShortBylineText2(
    val runs: List<Run11>?,
)

data class Run11(
    val text: String?,
    val navigationEndpoint: NavigationEndpoint9?,
)

data class NavigationEndpoint9(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata11?,
    val browseEndpoint: BrowseEndpoint7?,
)

data class CommandMetadata11(
    val webCommandMetadata: WebCommandMetadata11?,
)

data class WebCommandMetadata11(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint7(
    val browseId: String?,
    val canonicalBaseUrl: String?,
)

data class OwnerBadge(
    val metadataBadgeRenderer: MetadataBadgeRenderer?,
)

data class MetadataBadgeRenderer(
    val icon: Icon3?,
    val style: String?,
    val tooltip: String?,
    val trackingParams: String?,
    val accessibilityData: AccessibilityData2?,
)

data class Icon3(
    val iconType: String?,
)

data class AccessibilityData2(
    val label: String?,
)

data class Offlineability(
    val offlineabilityRenderer: OfflineabilityRenderer?,
)

data class OfflineabilityRenderer(
    val offlineable: Boolean?,
    val infoRenderer: InfoRenderer?,
    val clickTrackingParams: String?,
    val formats: List<Format>?,
)

data class InfoRenderer(
    val dismissableDialogRenderer: DismissableDialogRenderer?,
)

data class DismissableDialogRenderer(
    val dialogMessage: String?,
    val trackingParams: String?,
    val title: String?,
)

data class Format(
    val name: Name?,
    val formatType: String?,
)

data class Name(
    val runs: List<Run12>?,
)

data class Run12(
    val text: String?,
)

data class ShortViewCountText(
    val accessibility: Accessibility2?,
    val simpleText: String?,
)

data class Accessibility2(
    val accessibilityData: AccessibilityData3?,
)

data class AccessibilityData3(
    val label: String?,
)

data class Menu(
    val menuRenderer: MenuRenderer?,
)

data class MenuRenderer(
    val items: List<Item2>?,
    val trackingParams: String?,
    val accessibility: Accessibility3?,
)

data class Item2(
    val menuServiceItemRenderer: MenuServiceItemRenderer?,
)

data class MenuServiceItemRenderer(
    val text: Text4?,
    val icon: Icon4?,
    val serviceEndpoint: ServiceEndpoint?,
    val trackingParams: String?,
    val hasSeparator: Boolean?,
)

data class Text4(
    val runs: List<Run13>?,
)

data class Run13(
    val text: String?,
)

data class Icon4(
    val iconType: String?,
)

data class ServiceEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata12?,
    val signalServiceEndpoint: SignalServiceEndpoint?,
    val shareEntityServiceEndpoint: ShareEntityServiceEndpoint?,
)

data class CommandMetadata12(
    val webCommandMetadata: WebCommandMetadata12?,
)

data class WebCommandMetadata12(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class SignalServiceEndpoint(
    val signal: String?,
    val actions: List<Action>?,
)

data class Action(
    val clickTrackingParams: String?,
    val addToPlaylistCommand: AddToPlaylistCommand?,
)

data class AddToPlaylistCommand(
    val openMiniplayer: Boolean?,
    val videoId: String?,
    val listType: String?,
    val onCreateListCommand: OnCreateListCommand?,
    val videoIds: List<String>?,
)

data class OnCreateListCommand(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata13?,
    val createPlaylistServiceEndpoint: CreatePlaylistServiceEndpoint?,
)

data class CommandMetadata13(
    val webCommandMetadata: WebCommandMetadata13?,
)

data class WebCommandMetadata13(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class CreatePlaylistServiceEndpoint(
    val videoIds: List<String>?,
    val params: String?,
)

data class ShareEntityServiceEndpoint(
    val serializedShareEntity: String?,
    val commands: List<Command>?,
)

data class Command(
    val clickTrackingParams: String?,
    val openPopupAction: OpenPopupAction?,
)

data class OpenPopupAction(
    val popup: Popup?,
    val popupType: String?,
    val beReused: Boolean?,
)

data class Popup(
    val unifiedSharePanelRenderer: UnifiedSharePanelRenderer?,
)

data class UnifiedSharePanelRenderer(
    val trackingParams: String?,
    val showLoadingSpinner: Boolean?,
)

data class Accessibility3(
    val accessibilityData: AccessibilityData4?,
)

data class AccessibilityData4(
    val label: String?,
)

data class ThumbnailOverlay2(
    val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?,
    val thumbnailOverlayToggleButtonRenderer: ThumbnailOverlayToggleButtonRenderer?,
    val thumbnailOverlayNowPlayingRenderer: ThumbnailOverlayNowPlayingRenderer2?,
)

data class ThumbnailOverlayTimeStatusRenderer(
    val text: Text5?,
    val style: String?,
)

data class Text5(
    val accessibility: Accessibility4?,
    val simpleText: String?,
)

data class Accessibility4(
    val accessibilityData: AccessibilityData5?,
)

data class AccessibilityData5(
    val label: String?,
)

data class ThumbnailOverlayToggleButtonRenderer(
    val untoggledIcon: UntoggledIcon?,
    val toggledIcon: ToggledIcon?,
    val untoggledTooltip: String?,
    val toggledTooltip: String?,
    val untoggledServiceEndpoint: UntoggledServiceEndpoint?,
    val untoggledAccessibility: UntoggledAccessibility?,
    val toggledAccessibility: ToggledAccessibility?,
    val trackingParams: String?,
    val isToggled: Boolean?,
    val toggledServiceEndpoint: ToggledServiceEndpoint?,
)

data class UntoggledIcon(
    val iconType: String?,
)

data class ToggledIcon(
    val iconType: String?,
)

data class UntoggledServiceEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata14?,
    val signalServiceEndpoint: SignalServiceEndpoint2?,
    val playlistEditEndpoint: PlaylistEditEndpoint?,
)

data class CommandMetadata14(
    val webCommandMetadata: WebCommandMetadata14?,
)

data class WebCommandMetadata14(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class SignalServiceEndpoint2(
    val signal: String?,
    val actions: List<Action2>?,
)

data class Action2(
    val clickTrackingParams: String?,
    val addToPlaylistCommand: AddToPlaylistCommand2?,
)

data class AddToPlaylistCommand2(
    val openMiniplayer: Boolean?,
    val videoId: String?,
    val listType: String?,
    val onCreateListCommand: OnCreateListCommand2?,
    val videoIds: List<String>?,
)

data class OnCreateListCommand2(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata15?,
    val createPlaylistServiceEndpoint: CreatePlaylistServiceEndpoint2?,
)

data class CommandMetadata15(
    val webCommandMetadata: WebCommandMetadata15?,
)

data class WebCommandMetadata15(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class CreatePlaylistServiceEndpoint2(
    val videoIds: List<String>?,
    val params: String?,
)

data class PlaylistEditEndpoint(
    val playlistId: String?,
    val actions: List<Action3>?,
)

data class Action3(
    val addedVideoId: String?,
    val action: String?,
)

data class UntoggledAccessibility(
    val accessibilityData: AccessibilityData6?,
)

data class AccessibilityData6(
    val label: String?,
)

data class ToggledAccessibility(
    val accessibilityData: AccessibilityData7?,
)

data class AccessibilityData7(
    val label: String?,
)

data class ToggledServiceEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata16?,
    val playlistEditEndpoint: PlaylistEditEndpoint2?,
)

data class CommandMetadata16(
    val webCommandMetadata: WebCommandMetadata16?,
)

data class WebCommandMetadata16(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class PlaylistEditEndpoint2(
    val playlistId: String?,
    val actions: List<Action4>?,
)

data class Action4(
    val action: String?,
    val removedVideoId: String?,
)

data class ThumbnailOverlayNowPlayingRenderer2(
    val text: Text6?,
)

data class Text6(
    val runs: List<Run14>?,
)

data class Run14(
    val text: String?,
)

data class Badge(
    val metadataBadgeRenderer: MetadataBadgeRenderer2?,
)

data class MetadataBadgeRenderer2(
    val style: String?,
    val label: String?,
    val trackingParams: String?,
    val accessibilityData: AccessibilityData8?,
)

data class AccessibilityData8(
    val label: String?,
)

data class RichThumbnail(
    val movingThumbnailRenderer: MovingThumbnailRenderer?,
)

data class MovingThumbnailRenderer(
    val movingThumbnailDetails: MovingThumbnailDetails?,
    val enableHoveredLogging: Boolean?,
    val enableOverlay: Boolean?,
)

data class MovingThumbnailDetails(
    val thumbnails: List<Thumbnail10>?,
    val logAsMovingThumbnail: Boolean?,
)

data class Thumbnail10(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class NextButton(
    val buttonRenderer: ButtonRenderer?,
)

data class ButtonRenderer(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val icon: Icon5?,
    val accessibility: Accessibility5?,
    val trackingParams: String?,
)

data class Icon5(
    val iconType: String?,
)

data class Accessibility5(
    val label: String?,
)

data class PreviousButton(
    val buttonRenderer: ButtonRenderer2?,
)

data class ButtonRenderer2(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val icon: Icon6?,
    val accessibility: Accessibility6?,
    val trackingParams: String?,
)

data class Icon6(
    val iconType: String?,
)

data class Accessibility6(
    val label: String?,
)

data class PlayAllButton(
    val buttonRenderer: ButtonRenderer3?,
)

data class ButtonRenderer3(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val text: Text7?,
    val icon: Icon7?,
    val navigationEndpoint: NavigationEndpoint10?,
    val trackingParams: String?,
)

data class Text7(
    val runs: List<Run15>?,
)

data class Run15(
    val text: String?,
)

data class Icon7(
    val iconType: String?,
)

data class NavigationEndpoint10(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata17?,
    val watchEndpoint: WatchEndpoint5?,
)

data class CommandMetadata17(
    val webCommandMetadata: WebCommandMetadata17?,
)

data class WebCommandMetadata17(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint5(
    val videoId: String?,
    val playlistId: String?,
    val loggingContext: LoggingContext4?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig5?,
)

data class LoggingContext4(
    val vssLoggingContext: VssLoggingContext4?,
)

data class VssLoggingContext4(
    val serializedContextData: String?,
)

data class WatchEndpointSupportedOnesieConfig5(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig5?,
)

data class Html5PlaybackOnesieConfig5(
    val commonConfig: CommonConfig5?,
)

data class CommonConfig5(
    val url: String?,
)

data class Header(
    val carouselHeaderRenderer: CarouselHeaderRenderer?,
)

data class CarouselHeaderRenderer(
    val trackingParams: String?,
    val contents: List<Content5>?,
)

data class Content5(
    val carouselItemRenderer: CarouselItemRenderer?,
    val topicChannelDetailsRenderer: TopicChannelDetailsRenderer?,
)

data class CarouselItemRenderer(
    val carouselItems: List<CarouselItem>?,
    val backgroundColor: Long?,
    val trackingParams: String?,
    val layoutStyle: String?,
    val paginationThumbnails: List<PaginationThumbnail>?,
    val paginatorAlignment: String?,
)

data class CarouselItem(
    val defaultPromoPanelRenderer: DefaultPromoPanelRenderer?,
)

data class DefaultPromoPanelRenderer(
    val title: Title5?,
    val description: Description2?,
    val navigationEndpoint: NavigationEndpoint11?,
    val trackingParams: String?,
    val smallFormFactorBackgroundThumbnail: SmallFormFactorBackgroundThumbnail?,
    val largeFormFactorBackgroundThumbnail: LargeFormFactorBackgroundThumbnail?,
    val scrimColorValues: List<Long>?,
    val minPanelDisplayDurationMs: Long?,
    val minVideoPlayDurationMs: Long?,
    val inlinePlaybackRenderer: InlinePlaybackRenderer?,
    val scrimRotation: Long?,
    val metadataOrder: String?,
    val panelLayout: String?,
    val foregroundThumbnailDetails: ForegroundThumbnailDetails?,
    val maximumForegroundThumbnailHeight: Long?,
)

data class Title5(
    val runs: List<Run16>?,
    val accessibility: Accessibility7?,
)

data class Run16(
    val text: String?,
)

data class Accessibility7(
    val accessibilityData: AccessibilityData9?,
)

data class AccessibilityData9(
    val label: String?,
)

data class Description2(
    val runs: List<Run17>?,
)

data class Run17(
    val text: String?,
)

data class NavigationEndpoint11(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata18?,
    val watchEndpoint: WatchEndpoint6?,
)

data class CommandMetadata18(
    val webCommandMetadata: WebCommandMetadata18?,
)

data class WebCommandMetadata18(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint6(
    val videoId: String?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig6?,
)

data class WatchEndpointSupportedOnesieConfig6(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig6?,
)

data class Html5PlaybackOnesieConfig6(
    val commonConfig: CommonConfig6?,
)

data class CommonConfig6(
    val url: String?,
)

data class SmallFormFactorBackgroundThumbnail(
    val thumbnailLandscapePortraitRenderer: ThumbnailLandscapePortraitRenderer?,
)

data class ThumbnailLandscapePortraitRenderer(
    val landscape: Landscape?,
    val portrait: Portrait?,
)

data class Landscape(
    val thumbnails: List<Thumbnail11>?,
)

data class Thumbnail11(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Portrait(
    val thumbnails: List<Thumbnail12>?,
)

data class Thumbnail12(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class LargeFormFactorBackgroundThumbnail(
    val thumbnailLandscapePortraitRenderer: ThumbnailLandscapePortraitRenderer2?,
)

data class ThumbnailLandscapePortraitRenderer2(
    val landscape: Landscape2?,
    val portrait: Portrait2?,
)

data class Landscape2(
    val thumbnails: List<Thumbnail13>?,
)

data class Thumbnail13(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Portrait2(
    val thumbnails: List<Thumbnail13>?,
)


data class InlinePlaybackRenderer(
    val inlinePlaybackRenderer: InlinePlaybackRenderer2?,
)

data class InlinePlaybackRenderer2(
    val thumbnail: Thumbnail15?,
    val lengthText: LengthText?,
    val trackingParams: String?,
    val navigationEndpoint: NavigationEndpoint12?,
    val videoId: String?,
    val inlinePlaybackEndpoint: InlinePlaybackEndpoint?,
)

data class Thumbnail15(
    val thumbnails: List<Thumbnail16>?,
)

data class Thumbnail16(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class LengthText(
    val accessibility: Accessibility8?,
    val simpleText: String?,
)

data class Accessibility8(
    val accessibilityData: AccessibilityData10?,
)

data class AccessibilityData10(
    val label: String?,
)

data class NavigationEndpoint12(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata19?,
    val watchEndpoint: WatchEndpoint7?,
)

data class CommandMetadata19(
    val webCommandMetadata: WebCommandMetadata19?,
)

data class WebCommandMetadata19(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint7(
    val videoId: String?,
    val playerParams: String?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig7?,
)

data class WatchEndpointSupportedOnesieConfig7(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig7?,
)

data class Html5PlaybackOnesieConfig7(
    val commonConfig: CommonConfig7?,
)

data class CommonConfig7(
    val url: String?,
)

data class InlinePlaybackEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata20?,
    val watchEndpoint: WatchEndpoint8?,
)

data class CommandMetadata20(
    val webCommandMetadata: WebCommandMetadata20?,
)

data class WebCommandMetadata20(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class WatchEndpoint8(
    val videoId: String?,
    val startTimeSeconds: Long?,
    val playerParams: String?,
    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig8?,
)

data class WatchEndpointSupportedOnesieConfig8(
    val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig8?,
)

data class Html5PlaybackOnesieConfig8(
    val commonConfig: CommonConfig8?,
)

data class CommonConfig8(
    val url: String?,
)

data class ForegroundThumbnailDetails(
    val thumbnails: List<Thumbnail17>?,
)

data class Thumbnail17(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class PaginationThumbnail(
    val thumbnails: List<Thumbnail18>?,
    val accessibility: Accessibility9?,
)

data class Thumbnail18(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Accessibility9(
    val accessibilityData: AccessibilityData11?,
)

data class AccessibilityData11(
    val label: String?,
)

data class TopicChannelDetailsRenderer(
    val title: Title6?,
    val avatar: Avatar?,
    val subtitle: Subtitle?,
    val subscribeButton: SubscribeButton?,
    val navigationEndpoint: NavigationEndpoint13?,
    val trackingParams: String?,
)

data class Title6(
    val simpleText: String?,
)

data class Avatar(
    val thumbnails: List<Thumbnail19>?,
)

data class Thumbnail19(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Subtitle(
    val accessibility: Accessibility10?,
    val simpleText: String?,
)

data class Accessibility10(
    val accessibilityData: AccessibilityData12?,
)

data class AccessibilityData12(
    val label: String?,
)

data class SubscribeButton(
    val subscribeButtonRenderer: SubscribeButtonRenderer?,
)

data class SubscribeButtonRenderer(
    val buttonText: ButtonText?,
    val subscriberCountText: SubscriberCountText?,
    val subscribed: Boolean?,
    val enabled: Boolean?,
    val type: String?,
    val channelId: String?,
    val showPreferences: Boolean?,
    val subscribedButtonText: SubscribedButtonText?,
    val unsubscribedButtonText: UnsubscribedButtonText?,
    val trackingParams: String?,
    val unsubscribeButtonText: UnsubscribeButtonText?,
    val subscribeAccessibility: SubscribeAccessibility?,
    val unsubscribeAccessibility: UnsubscribeAccessibility?,
    val subscribedEntityKey: String?,
    val onSubscribeEndpoints: List<OnSubscribeEndpoint>?,
    val onUnsubscribeEndpoints: List<OnUnsubscribeEndpoint>?,
)

data class ButtonText(
    val runs: List<Run18>?,
)

data class Run18(
    val text: String?,
)

data class SubscriberCountText(
    val accessibility: Accessibility11?,
    val simpleText: String?,
)

data class Accessibility11(
    val accessibilityData: AccessibilityData13?,
)

data class AccessibilityData13(
    val label: String?,
)

data class SubscribedButtonText(
    val runs: List<Run19>?,
)

data class Run19(
    val text: String?,
)

data class UnsubscribedButtonText(
    val runs: List<Run20>?,
)

data class Run20(
    val text: String?,
)

data class UnsubscribeButtonText(
    val runs: List<Run21>?,
)

data class Run21(
    val text: String?,
)

data class SubscribeAccessibility(
    val accessibilityData: AccessibilityData14?,
)

data class AccessibilityData14(
    val label: String?,
)

data class UnsubscribeAccessibility(
    val accessibilityData: AccessibilityData15?,
)

data class AccessibilityData15(
    val label: String?,
)

data class OnSubscribeEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata21?,
    val subscribeEndpoint: SubscribeEndpoint?,
)

data class CommandMetadata21(
    val webCommandMetadata: WebCommandMetadata21?,
)

data class WebCommandMetadata21(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class SubscribeEndpoint(
    val channelIds: List<String>?,
    val params: String?,
)

data class OnUnsubscribeEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata22?,
    val signalServiceEndpoint: SignalServiceEndpoint3?,
)

data class CommandMetadata22(
    val webCommandMetadata: WebCommandMetadata22?,
)

data class WebCommandMetadata22(
    val sendPost: Boolean?,
)

data class SignalServiceEndpoint3(
    val signal: String?,
    val actions: List<Action5>?,
)

data class Action5(
    val clickTrackingParams: String?,
    val openPopupAction: OpenPopupAction2?,
)

data class OpenPopupAction2(
    val popup: Popup2?,
    val popupType: String?,
)

data class Popup2(
    val confirmDialogRenderer: ConfirmDialogRenderer?,
)

data class ConfirmDialogRenderer(
    val trackingParams: String?,
    val dialogMessages: List<DialogMessage>?,
    val confirmButton: ConfirmButton?,
    val cancelButton: CancelButton?,
    val primaryIsCancel: Boolean?,
)

data class DialogMessage(
    val runs: List<Run22>?,
)

data class Run22(
    val text: String?,
)

data class ConfirmButton(
    val buttonRenderer: ButtonRenderer4?,
)

data class ButtonRenderer4(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val text: Text8?,
    val serviceEndpoint: ServiceEndpoint2?,
    val accessibility: Accessibility12?,
    val trackingParams: String?,
)

data class Text8(
    val runs: List<Run23>?,
)

data class Run23(
    val text: String?,
)

data class ServiceEndpoint2(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata23?,
    val unsubscribeEndpoint: UnsubscribeEndpoint?,
)

data class CommandMetadata23(
    val webCommandMetadata: WebCommandMetadata23?,
)

data class WebCommandMetadata23(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class UnsubscribeEndpoint(
    val channelIds: List<String>?,
    val params: String?,
)

data class Accessibility12(
    val label: String?,
)

data class CancelButton(
    val buttonRenderer: ButtonRenderer5?,
)

data class ButtonRenderer5(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val text: Text9?,
    val accessibility: Accessibility13?,
    val trackingParams: String?,
)

data class Text9(
    val runs: List<Run24>?,
)

data class Run24(
    val text: String?,
)

data class Accessibility13(
    val label: String?,
)

data class NavigationEndpoint13(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata24?,
    val browseEndpoint: BrowseEndpoint8?,
)

data class CommandMetadata24(
    val webCommandMetadata: WebCommandMetadata24?,
)

data class WebCommandMetadata24(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint8(
    val browseId: String?,
    val canonicalBaseUrl: String?,
)

data class Metadata(
    val channelMetadataRenderer: ChannelMetadataRenderer?,
)

data class ChannelMetadataRenderer(
    val title: String?,
    val description: String?,
    val rssUrl: String?,
    val externalId: String?,
    val keywords: String?,
    val ownerUrls: List<String>?,
    val avatar: Avatar2?,
    val channelUrl: String?,
    val isFamilySafe: Boolean?,
    val availableCountryCodes: List<String>?,
    val androidDeepLink: String?,
    val androidAppindexingLink: String?,
    val iosAppindexingLink: String?,
    val vanityChannelUrl: String?,
)

data class Avatar2(
    val thumbnails: List<Thumbnail20>?,
)

data class Thumbnail20(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class Topbar(
    val desktopTopbarRenderer: DesktopTopbarRenderer?,
)

data class DesktopTopbarRenderer(
    val logo: Logo?,
    val searchbox: Searchbox?,
    val trackingParams: String?,
    val countryCode: String?,
    val topbarButtons: List<TopbarButton>?,
    val hotkeyDialog: HotkeyDialog?,
    val backButton: BackButton?,
    val forwardButton: ForwardButton?,
    val a11ySkipNavigationButton: A11ySkipNavigationButton?,
    val voiceSearchButton: VoiceSearchButton?,
)

data class Logo(
    val topbarLogoRenderer: TopbarLogoRenderer?,
)

data class TopbarLogoRenderer(
    val iconImage: IconImage?,
    val tooltipText: TooltipText?,
    val endpoint: Endpoint3?,
    val trackingParams: String?,
    val overrideEntityKey: String?,
)

data class IconImage(
    val iconType: String?,
)

data class TooltipText(
    val runs: List<Run25>?,
)

data class Run25(
    val text: String?,
)

data class Endpoint3(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata25?,
    val browseEndpoint: BrowseEndpoint9?,
)

data class CommandMetadata25(
    val webCommandMetadata: WebCommandMetadata25?,
)

data class WebCommandMetadata25(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
    val apiUrl: String?,
)

data class BrowseEndpoint9(
    val browseId: String?,
)

data class Searchbox(
    val fusionSearchboxRenderer: FusionSearchboxRenderer?,
)

data class FusionSearchboxRenderer(
    val icon: Icon8?,
    val placeholderText: PlaceholderText?,
    val config: Config?,
    val trackingParams: String?,
    val searchEndpoint: SearchEndpoint?,
    val clearButton: ClearButton?,
)

data class Icon8(
    val iconType: String?,
)

data class PlaceholderText(
    val runs: List<Run26>?,
)

data class Run26(
    val text: String?,
)

data class Config(
    val webSearchboxConfig: WebSearchboxConfig?,
)

data class WebSearchboxConfig(
    val requestLanguage: String?,
    val requestDomain: String?,
    val hasOnscreenKeyboard: Boolean?,
    val focusSearchbox: Boolean?,
)

data class SearchEndpoint(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata26?,
    val searchEndpoint: SearchEndpoint2?,
)

data class CommandMetadata26(
    val webCommandMetadata: WebCommandMetadata26?,
)

data class WebCommandMetadata26(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class SearchEndpoint2(
    val query: String?,
)

data class ClearButton(
    val buttonRenderer: ButtonRenderer6?,
)

data class ButtonRenderer6(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val icon: Icon9?,
    val trackingParams: String?,
    val accessibilityData: AccessibilityData16?,
)

data class Icon9(
    val iconType: String?,
)

data class AccessibilityData16(
    val accessibilityData: AccessibilityData17?,
)

data class AccessibilityData17(
    val label: String?,
)

data class TopbarButton(
    val topbarMenuButtonRenderer: TopbarMenuButtonRenderer?,
    val buttonRenderer: ButtonRenderer7?,
)

data class TopbarMenuButtonRenderer(
    val icon: Icon10?,
    val menuRequest: MenuRequest?,
    val trackingParams: String?,
    val accessibility: Accessibility14?,
    val tooltip: String?,
    val style: String?,
)

data class Icon10(
    val iconType: String?,
)

data class MenuRequest(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata27?,
    val signalServiceEndpoint: SignalServiceEndpoint4?,
)

data class CommandMetadata27(
    val webCommandMetadata: WebCommandMetadata27?,
)

data class WebCommandMetadata27(
    val sendPost: Boolean?,
    val apiUrl: String?,
)

data class SignalServiceEndpoint4(
    val signal: String?,
    val actions: List<Action6>?,
)

data class Action6(
    val clickTrackingParams: String?,
    val openPopupAction: OpenPopupAction3?,
)

data class OpenPopupAction3(
    val popup: Popup3?,
    val popupType: String?,
    val beReused: Boolean?,
)

data class Popup3(
    val multiPageMenuRenderer: MultiPageMenuRenderer?,
)

data class MultiPageMenuRenderer(
    val trackingParams: String?,
    val style: String?,
    val showLoadingSpinner: Boolean?,
)

data class Accessibility14(
    val accessibilityData: AccessibilityData18?,
)

data class AccessibilityData18(
    val label: String?,
)

data class ButtonRenderer7(
    val style: String?,
    val size: String?,
    val text: Text10?,
    val icon: Icon11?,
    val navigationEndpoint: NavigationEndpoint14?,
    val trackingParams: String?,
    val targetId: String?,
)

data class Text10(
    val runs: List<Run27>?,
)

data class Run27(
    val text: String?,
)

data class Icon11(
    val iconType: String?,
)

data class NavigationEndpoint14(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata28?,
    val signInEndpoint: SignInEndpoint?,
)

data class CommandMetadata28(
    val webCommandMetadata: WebCommandMetadata28?,
)

data class WebCommandMetadata28(
    val url: String?,
    val webPageType: String?,
    val rootVe: Long?,
)

data class SignInEndpoint(
    val idamTag: String?,
)

data class HotkeyDialog(
    val hotkeyDialogRenderer: HotkeyDialogRenderer?,
)

data class HotkeyDialogRenderer(
    val title: Title7?,
    val sections: List<Section>?,
    val dismissButton: DismissButton?,
    val trackingParams: String?,
)

data class Title7(
    val runs: List<Run28>?,
)

data class Run28(
    val text: String?,
)

data class Section(
    val hotkeyDialogSectionRenderer: HotkeyDialogSectionRenderer?,
)

data class HotkeyDialogSectionRenderer(
    val title: Title8?,
    val options: List<Option>?,
)

data class Title8(
    val runs: List<Run29>?,
)

data class Run29(
    val text: String?,
)

data class Option(
    val hotkeyDialogSectionOptionRenderer: HotkeyDialogSectionOptionRenderer?,
)

data class HotkeyDialogSectionOptionRenderer(
    val label: Label?,
    val hotkey: String?,
    val hotkeyAccessibilityLabel: HotkeyAccessibilityLabel?,
)

data class Label(
    val runs: List<Run30>?,
)

data class Run30(
    val text: String?,
)

data class HotkeyAccessibilityLabel(
    val accessibilityData: AccessibilityData19?,
)

data class AccessibilityData19(
    val label: String?,
)

data class DismissButton(
    val buttonRenderer: ButtonRenderer8?,
)

data class ButtonRenderer8(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val text: Text11?,
    val trackingParams: String?,
)

data class Text11(
    val runs: List<Run31>?,
)

data class Run31(
    val text: String?,
)

data class BackButton(
    val buttonRenderer: ButtonRenderer9?,
)

data class ButtonRenderer9(
    val trackingParams: String?,
    val command: Command2?,
)

data class Command2(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata29?,
    val signalServiceEndpoint: SignalServiceEndpoint5?,
)

data class CommandMetadata29(
    val webCommandMetadata: WebCommandMetadata29?,
)

data class WebCommandMetadata29(
    val sendPost: Boolean?,
)

data class SignalServiceEndpoint5(
    val signal: String?,
    val actions: List<Action7>?,
)

data class Action7(
    val clickTrackingParams: String?,
    val signalAction: SignalAction?,
)

data class SignalAction(
    val signal: String?,
)

data class ForwardButton(
    val buttonRenderer: ButtonRenderer10?,
)

data class ButtonRenderer10(
    val trackingParams: String?,
    val command: Command3?,
)

data class Command3(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata30?,
    val signalServiceEndpoint: SignalServiceEndpoint6?,
)

data class CommandMetadata30(
    val webCommandMetadata: WebCommandMetadata30?,
)

data class WebCommandMetadata30(
    val sendPost: Boolean?,
)

data class SignalServiceEndpoint6(
    val signal: String?,
    val actions: List<Action8>?,
)

data class Action8(
    val clickTrackingParams: String?,
    val signalAction: SignalAction2?,
)

data class SignalAction2(
    val signal: String?,
)

data class A11ySkipNavigationButton(
    val buttonRenderer: ButtonRenderer11?,
)

data class ButtonRenderer11(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val text: Text12?,
    val trackingParams: String?,
    val command: Command4?,
)

data class Text12(
    val runs: List<Run32>?,
)

data class Run32(
    val text: String?,
)

data class Command4(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata31?,
    val signalServiceEndpoint: SignalServiceEndpoint7?,
)

data class CommandMetadata31(
    val webCommandMetadata: WebCommandMetadata31?,
)

data class WebCommandMetadata31(
    val sendPost: Boolean?,
)

data class SignalServiceEndpoint7(
    val signal: String?,
    val actions: List<Action9>?,
)

data class Action9(
    val clickTrackingParams: String?,
    val signalAction: SignalAction3?,
)

data class SignalAction3(
    val signal: String?,
)

data class VoiceSearchButton(
    val buttonRenderer: ButtonRenderer12?,
)

data class ButtonRenderer12(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val serviceEndpoint: ServiceEndpoint3?,
    val icon: Icon13?,
    val tooltip: String?,
    val trackingParams: String?,
    val accessibilityData: AccessibilityData22?,
)

data class ServiceEndpoint3(
    val clickTrackingParams: String?,
    val commandMetadata: CommandMetadata32?,
    val signalServiceEndpoint: SignalServiceEndpoint8?,
)

data class CommandMetadata32(
    val webCommandMetadata: WebCommandMetadata32?,
)

data class WebCommandMetadata32(
    val sendPost: Boolean?,
)

data class SignalServiceEndpoint8(
    val signal: String?,
    val actions: List<Action10>?,
)

data class Action10(
    val clickTrackingParams: String?,
    val openPopupAction: OpenPopupAction4?,
)

data class OpenPopupAction4(
    val popup: Popup4?,
    val popupType: String?,
)

data class Popup4(
    val voiceSearchDialogRenderer: VoiceSearchDialogRenderer?,
)

data class VoiceSearchDialogRenderer(
    val placeholderHeader: PlaceholderHeader?,
    val promptHeader: PromptHeader?,
    val exampleQuery1: ExampleQuery1?,
    val exampleQuery2: ExampleQuery2?,
    val promptMicrophoneLabel: PromptMicrophoneLabel?,
    val loadingHeader: LoadingHeader?,
    val connectionErrorHeader: ConnectionErrorHeader?,
    val connectionErrorMicrophoneLabel: ConnectionErrorMicrophoneLabel?,
    val permissionsHeader: PermissionsHeader?,
    val permissionsSubtext: PermissionsSubtext?,
    val disabledHeader: DisabledHeader?,
    val disabledSubtext: DisabledSubtext?,
    val microphoneButtonAriaLabel: MicrophoneButtonAriaLabel?,
    val exitButton: ExitButton?,
    val trackingParams: String?,
    val microphoneOffPromptHeader: MicrophoneOffPromptHeader?,
)

data class PlaceholderHeader(
    val runs: List<Run33>?,
)

data class Run33(
    val text: String?,
)

data class PromptHeader(
    val runs: List<Run34>?,
)

data class Run34(
    val text: String?,
)

data class ExampleQuery1(
    val runs: List<Run35>?,
)

data class Run35(
    val text: String?,
)

data class ExampleQuery2(
    val runs: List<Run36>?,
)

data class Run36(
    val text: String?,
)

data class PromptMicrophoneLabel(
    val runs: List<Run37>?,
)

data class Run37(
    val text: String?,
)

data class LoadingHeader(
    val runs: List<Run38>?,
)

data class Run38(
    val text: String?,
)

data class ConnectionErrorHeader(
    val runs: List<Run39>?,
)

data class Run39(
    val text: String?,
)

data class ConnectionErrorMicrophoneLabel(
    val runs: List<Run40>?,
)

data class Run40(
    val text: String?,
)

data class PermissionsHeader(
    val runs: List<Run41>?,
)

data class Run41(
    val text: String?,
)

data class PermissionsSubtext(
    val runs: List<Run42>?,
)

data class Run42(
    val text: String?,
)

data class DisabledHeader(
    val runs: List<Run43>?,
)

data class Run43(
    val text: String?,
)

data class DisabledSubtext(
    val runs: List<Run44>?,
)

data class Run44(
    val text: String?,
)

data class MicrophoneButtonAriaLabel(
    val runs: List<Run45>?,
)

data class Run45(
    val text: String?,
)

data class ExitButton(
    val buttonRenderer: ButtonRenderer13?,
)

data class ButtonRenderer13(
    val style: String?,
    val size: String?,
    val isDisabled: Boolean?,
    val icon: Icon12?,
    val trackingParams: String?,
    val accessibilityData: AccessibilityData20?,
)

data class Icon12(
    val iconType: String?,
)

data class AccessibilityData20(
    val accessibilityData: AccessibilityData21?,
)

data class AccessibilityData21(
    val label: String?,
)

data class MicrophoneOffPromptHeader(
    val runs: List<Run46>?,
)

data class Run46(
    val text: String?,
)

data class Icon13(
    val iconType: String?,
)

data class AccessibilityData22(
    val accessibilityData: AccessibilityData23?,
)

data class AccessibilityData23(
    val label: String?,
)

data class Microformat(
    val microformatDataRenderer: MicroformatDataRenderer?,
)

data class MicroformatDataRenderer(
    val urlCanonical: String?,
    val title: String?,
    val description: String?,
    val thumbnail: Thumbnail21?,
    val siteName: String?,
    val appName: String?,
    val androidPackage: String?,
    val iosAppStoreId: String?,
    val iosAppArguments: String?,
    val ogType: String?,
    val urlApplinksWeb: String?,
    val urlApplinksIos: String?,
    val urlApplinksAndroid: String?,
    val urlTwitterIos: String?,
    val urlTwitterAndroid: String?,
    val twitterCardType: String?,
    val twitterSiteHandle: String?,
    val schemaDotOrgType: String?,
    val noindex: Boolean?,
    val unlisted: Boolean?,
    val familySafe: Boolean?,
    val availableCountries: List<String>?,
    val linkAlternates: List<LinkAlternate>?,
)

data class Thumbnail21(
    val thumbnails: List<Thumbnail22>?,
)

data class Thumbnail22(
    val url: String?,
    val width: Long?,
    val height: Long?,
)

data class LinkAlternate(
    val hrefUrl: String?,
)

data class FrameworkUpdates(
    val entityBatchUpdate: EntityBatchUpdate?,
)

data class EntityBatchUpdate(
    val mutations: List<Mutation>?,
    val timestamp: Timestamp?,
)

data class Mutation(
    val entityKey: String?,
    val type: String?,
    val payload: Payload?,
)

data class Payload(
    val subscriptionStateEntity: SubscriptionStateEntity?,
)

data class SubscriptionStateEntity(
    val key: String?,
    val subscribed: Boolean?,
)

data class Timestamp(
    val seconds: String?,
    val nanos: Long?,
)
