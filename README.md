# CatanDice

An app I made after a particularly bad Catan game. I desired for a set of dice that is artificially made "fairer" than real physical dice. By "fairer" I mean that the relative frequency of each outcome doesn't deviate too far from the others.

I tried out various ways of achieving that and settled on a system where I separate the possible future tosses into layers based on how many times each combination has already been rolled. In each toss one of the layers and one toss from that layer are uniformly selected. The idea is, that the least frequent combinations appear in all layers, whereas the combinations that had so far been tossed the most only appear in the top layer(s).
This turns out to be a good system as it makes long unlucky streaks nearly impossible, while still preserving the random element in the game and preventing players from guessing the outcomes of future tosses.

![Screenshot of the app](https://github.com/andyElking/CatanDice/blob/eff1ca51a47cd2a871ce7877333ca61d8cee82bc/Screenshot%20from%202021-09-17%2020-22-09.png)
