# GhostMapper Pro — protótipo Android nativo

Projeto Android Studio em Kotlin. **Não é uma cópia nem substituto completo do GG Mouse Pro.**

## O que funciona no código
- Tela de configuração para permissões.
- Serviço de acessibilidade com captura de teclas físicas quando o Android as encaminha ao serviço.
- Conversão de tecla configurada em gesto de toque via `dispatchGesture`.
- Editor flutuante para adicionar uma tecla e coordenadas X/Y (0–100%).
- Persistência local de mapeamentos.

## Como compilar
1. Abra a pasta `GhostMapperPro` no Android Studio (JDK 17, Android SDK 35).
2. Sincronize o Gradle com a internet.
3. Selecione **Build > Build APK(s)**.
4. Instale o APK gerado em `app/build/outputs/apk/debug/`.
5. Abra o aplicativo e autorize sobreposição e serviço de acessibilidade.
6. Abra o editor, cadastre a tecla (por exemplo `J`) e posição (80,70), salve e feche o editor.

## Restrições importantes
- Não testado em dispositivo real e não há APK pré-compilado neste ZIP.
- O Android pode reservar ou bloquear eventos de teclado/mouse; nem todos os jogos aceitam gestos de acessibilidade.
- Ainda faltam: movimento relativo de mouse, mira FPS, WASD analógico, gamepads, perfis por aplicativo, importação/exportação e interface gráfica refinada.
- O editor usa coordenadas percentuais da tela inteira e não considera barras do sistema nem rotação.
- Use somente em jogos que permitam mapeadores externos, respeitando regras e políticas dos jogos.
- A acessibilidade deve ser ativada manualmente pelo usuário; não há contorno de proteções do Android.
