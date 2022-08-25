# j2meui
Declarative UI framework for Java ME powered phones. Inspired by Apple's SwiftUI. Requires only MIDP 1.0/CLDC 1.1 so is compatible with most (if not all) of the phones. This framework makes use of CombiME - reactive programming library for mobile phones, inspired by Apple's Combine.

Due to limitations of the MIDP 1.0 is split into two parts: UI-part, to build forms with standard components only, and CG-part, providing custom drawing capabilities and inplementing your own controls. Layout in CG is done via stacks: HStack, VStack, ZStack. Also you may specify raw frames if you feel like to.

![22123123123](https://user-images.githubusercontent.com/13520824/186643006-10f1084d-e90d-49e6-a34b-7cf41b3f83e2.png)
