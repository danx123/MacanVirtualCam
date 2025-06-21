# 🐅 MacanVirtualCam
Virtual camera siluman Android via WebRTC loopback + OBS bridge.  
📡 Tanpa root. Tanpa capture layar. Langsung injek playback ke kamera palsu.

## 🔧 Fitur
- WebRTC peer streaming
- SurfaceTexture injection
- OBS VirtualCam bridge
- SDP pairing manual / WebSocket

## 🚀 Cara Pakai
1. Jalankan app Android
2. Copy SDP offer ke `local_viewer.html`
3. Ambil SDP answer → inject ke app
4. OBS tangkap → VirtualCam aktif!

## 📁 Struktur
- `webrtc/` → MacanPeer & launcher
- `render/` → Surface injection
- `assets/` → Viewer HTML (optional)

## 🧙‍♂️ Credits
Powered by WebRTC, OBS, dan semangat loreng digital.


---

## 📦 Cara Pakai

1. Clone repo ini:
git clone https://github.com/danx123/MacanKontrolPanel.git

2. Buka di Android Studio (minimal SDK 21)

3. Hubungkan ke `MacanVirtualCam`:
- via intent (Android)
- atau WebSocket (jika standalone dashboard HTML)

---

## 🌐 Integrasi Viewer

Jika pairing via WebRTC:
- Kirim `offer` dari `MacanVirtualCam`
- Panel akan tampilkan dan kirim ke `local_viewer.html` via WebSocket
- Jawaban `answer` bisa dimasukkan ulang otomatis

---

## 🧙‍♂️ Credits

- Dibuat khusus oleh Macan Dev untuk mengontrol peer streaming siluman
- Terinspirasi oleh wire-level kontrol WebRTC dan obs-kernel injection

---

## 📄 License

MIT

