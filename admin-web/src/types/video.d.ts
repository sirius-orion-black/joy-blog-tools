export interface TrackVideoState {
  src: string
  kind: 'subtitles' | 'captions' | 'descriptions' | 'chapters' | 'metadata'
  srclang?: string
  label?: string
}

export interface PropsVideoState {
  src: string
  width?: string
  height?: string
  posterUrl?: string
  preload?: 'none' | 'metadata' | 'auto'
  autoPlay?: boolean
  controls?: boolean
  loop?: boolean
  muted?: boolean
  tracks?: TrackVideoState[]
}
