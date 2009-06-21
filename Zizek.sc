Zizek {var <>pianoAddr, <>drumsAddr, <>bassAddr, s, basicPath, <>midi, funcPlay, funcNoteOn, <>yellowLock, <>sceneLock, <>scoreElec, <>scorePiano, <>scoreDrums, <>scoreBass;

	*new {arg ipAddr1 = "localhost", ipAddr2 = "localhost", ipAddr3 = "localhost";
	^super.new.initZizek(ipAddr1, ipAddr2, ipAddr3);
	}
	
	initZizek {arg ipAddr1 = "localhost", ipAddr2 = "localhost", ipAddr3 = "localhost";
	//network
	pianoAddr = NetAddr(ipAddr1, 57120); // the url should be the one of computer 1 
	drumsAddr = NetAddr(ipAddr2, 57120); // the url should be the one of computer 2
	bassAddr = NetAddr(ipAddr3, 57120); // the url should be the one of computer 3
	
	basicPath = Document.current.path.dirname;
	
	s = Server.default; //set s to default server
	
	//read midifile
	midi = SimpleMIDIFile(basicPath ++  "/ockeghem.mid");
	midi.read;
	midi.timeMode = \seconds;
	midi.convertNoteOns(0);
	
	yellowLock = [1,1,1]; //lock for yellow letters
	sceneLock = [1,1,1]; //lock for scene letters
	
	this.score;
	
	Buffer.read(s, basicPath ++ "/ZizekScore/piano.aif",  bufnum:1);
	Buffer.read(s, basicPath ++ "/ZizekScore/drums.aif",  bufnum:2);
	Buffer.read(s, basicPath ++ "/ZizekScore/bass.aif",  bufnum:3);
	}
	
	score {	
	scoreElec = [{arg section;
	section.postln;
	}, {arg silence;
	silence.postln;
	}];
	
	scorePiano = [
	//section function:
	{arg section;
	section.postln;
	//send score info to piano through OSC
	//locks for skiping text for animations
	case
	{(section >= 1).and(section <= 71)} {
	yellowLock[0] = 1;
	sceneLock[0] = 1;
	}
	{(section >= 72).and(section <= 75)} {
	yellowLock[0] = 0;
	sceneLock[0] = 0;
	}
	{(section >= 76).and(section <= 85)} {
	yellowLock[0] = 1;
	sceneLock[0] = 1;
	};
	case
	{(section >= 1).and(section <= 31)} { //send 'like' direction in these sections
	this.funcOSC(\piano, \like);} 
	{(section >= 32).and(section <= 37)} { //send 'with' direction in these sections
	this.funcOSC(\piano, \with);}
	{(section >= 38).and(section <= 58)} { //send 'with' direction in these sections
	this.funcOSC(\piano, \free);}
	{(section >= 59).and(section <= 64)} { //send 'with' direction in these sections
	this.funcOSC(\piano, \solo);}
	{(section >= 65).and(section <= 71)} { //send 'with' direction in these sections
	this.funcOSC(\piano, \unlike);}
	{section == 72} {
	this.funcOSC(\piano, \animation);}
	{(section >= 76).and(section <= 85)} { //send 'with' direction in these sections
	this.funcOSC(\piano, \without);}
	; 
	//silence function:
	}, {arg silence;
	silence.postln;
	if([72,73,74].includes(silence.asInteger), {"No Silence : Animation".postln}, {
	if(silence < 85, { 
	this.funcOSC(\piano, \silence);
	})
	});
	if(silence == 75, {
	this.funcOSC(\piano, \scene5);
	});
	}
	];
	
	scoreDrums = [ 
	//section function:
	{arg section; 
	section.postln;
	//send score info to drums through OSC
	//locks for skiping text for animations
	case
	{(section >= 1).and(section <= 49)} {
	yellowLock[1] = 1;
	sceneLock[1] = 1;
	}
	{(section >= 50).and(section <= 51)} {
	yellowLock[1] = 0;
	sceneLock[1] = 0;
	}
	{(section >= 52).and(section <= 54)} {
	yellowLock[1] = 1;
	sceneLock[1] = 1;
	};
	case
	{(section >= 1).and(section <= 25)} { //send 'like' direction in these sections
	this.funcOSC(\drums, \like);} 
	{(section >= 26).and(section <= 33)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \with);}
	{(section >= 34).and(section <= 37)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \free);}
	{section == 38} { //send 'with' direction in these sections
	this.funcOSC(\drums, \solo);}
	{(section >= 39).and(section <= 41)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \free);}
	{(section >= 42).and(section <= 43)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \solo);}
	{(section >= 44).and(section <= 49)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \unlike);}
	{section == 50} {
	this.funcOSC(\drums, \animation);}
	{(section >= 52).and(section <= 54)} { //send 'with' direction in these sections
	this.funcOSC(\drums, \without);}
	;
	//silence function:
	}, {arg silence; 
	silence.postln; 
	if([50].includes(silence.asInteger), {"No Silence : Animation".postln}, { 
	if(silence < 54, {
	this.funcOSC(\drums, \silence);
	});
	});
	if(silence == 51, {
	this.funcOSC(\drums, \scene5);
	});
	if(silence == 54, {
	this.funcOSCAll(\silence);
	Routine({1.do({
	5.yield;
	this.funcOSCAll(\clear2);
	this.funcOSCAll(\end);
	});}).play;
	});
	}
	];
	
	scoreBass = [
	{arg section; 
	section.postln;
	//send score info to bass through OSC
	//locks for skiping text for animations
	case
	{(section >= 1).and(section <= 60)} {
	yellowLock[2] = 1;
	sceneLock[2] = 1;
	}
	{(section >= 61).and(section <= 63)} {
	yellowLock[2] = 0;
	sceneLock[2] = 0;
	}
	{(section >= 64).and(section <= 72)} {
	yellowLock[2] = 1;
	sceneLock[2] = 1;
	};
	case
	{(section >= 1).and(section <= 28)} { //send 'like' direction in these sections
	this.funcOSC(\bass, \like);} 
	{(section >= 29).and(section <= 36)} { //send 'with' direction in these sections
	this.funcOSC(\bass, \with);} 
	{(section >= 37).and(section <= 50)} { //send 'with' direction in these sections
	this.funcOSC(\bass, \free);} 
	{(section >= 51).and(section <= 53)} { //send 'with' direction in these sections
	this.funcOSC(\bass, \solo);} 
	{(section >= 54).and(section <= 60)} { //send 'with' direction in these sections
	this.funcOSC(\bass, \unlike);} 
	{section == 61} {
	this.funcOSC(\bass, \animation);}
	{(section >= 64).and(section <= 72)} {
	this.funcOSC(\bass, \without);}
	;
	//silence function:
	}, {arg silence; 
	silence.postln; 
	if([61,62].includes(silence.asInteger), {"No Silence : Animation".postln}, { 
	if(silence < 72, {
	this.funcOSC(\bass, \silence);
	});
	});
	if(silence == 63, {
	this.funcOSC(\bass, \scene5);
	});
	}
	];
}
	
	play {arg startTime=0;
	
	this.funcSection(\elec, scoreElec[0], scoreElec[1], startTime);
	this.funcSection(\piano, scorePiano[0], scorePiano[1], startTime);
	this.funcSection(\drums, scoreDrums[0], scoreDrums[1], startTime);
	this.funcSection(\bass, scoreBass[0], scoreBass[1], startTime);

	}
	
	clearAll {
	this.funcOSCAll(\clear);
	this.funcOSCAll(\clear2);
	}
	
	playZizek {arg start=0, amp=1;
	var startElec, startRout;
	startElec = if(start < 20, {0}, {start - 20});
	startRout = if(start < 20, {20-start}, {startRout = 0;});
	this.funcOSCAll(start);
	
	
	this.funcOSCAll(\clear);
	
	Routine({1.do({
	
	Synth("\playHeadphones", [\out, 0, \bufnum, 1, \start, start, \amp, amp]);
	Synth("\playHeadphones", [\out, 1, \bufnum, 2, \start, start, \amp, amp]);
	Synth("\playHeadphones", [\out, 2, \bufnum, 3, \start, start, \amp, amp]);
	
	
	this.playScenes(start);
	"Lacan Music".postln;
	startRout.yield;
	"start playing".postln;
	this.play(startElec);
	}); }).play;

	
	}
	
	//gets track number from instrument	
	funcInst {arg instrument;
	var track;
	case
	{instrument == \elec} {track = 1}
	{instrument == \piano} {track = 2}
	{instrument == \drums} {track = 3}
	{instrument == \bass} {track = 4};
	^track;
	}
	
	funcSection {arg instrument, func= {arg sec; sec.postln;}, funcSilence = {arg silence; silence.postln;}, newTime=0, adjTempo=1;
	var track, arr, yellowLight, step=0;
		
	track = this.funcInst(instrument);
	
	arr = midi.trackSilence(track).flop[0];
	arr = arr - newTime; //minus newTime
	arr = arr.reject({|item| item.isNegative }); //reject negative to get new time arr

	yellowLight = (arr - 2.5).reject({|item| item < 0});
	
	yellowLight = yellowLight.differentiate;
	
	midi.sectionPlay(track, {arg val;
	var section, silence;
	instrument.post;
	if(val.odd, {" : Play: section: ".post;
	//this is the sections starting with 1 (for better order);
	section = val+1/2;
	//specific info
	func.value(section);
	}, {
	" : Silence: ".post;
	silence = val/2;
	funcSilence.value(silence);
	}); 
	}, 
	newTime, //this is the new step 
	adjTempo);
	if(instrument != \elec, { //if instrument is not electronics, then send OSC
	//send yellow letters
	Routine({yellowLight.size.do({
	yellowLight[step].yield;
	if(yellowLock[track-2] == 1, {
	this.funcOSC(instrument, \yellow);
	});
	step = step + 1;
	})}).play;	
	});
	}

	
	//function to send OSC messages to right instruments
	funcOSC {arg instrument, direction; 
	var variable, id;
	case
	{instrument == \piano} {variable = pianoAddr; id = '/piano'}
	{instrument == \drums} {variable = drumsAddr; id = '/drums'}
	{instrument == \bass} {variable = bassAddr; id = '/bass'};
	variable.sendMsg(id, direction); 
	} //send info to correct instrument

	funcOSCAll {arg direction, ignore=[1,1,1];
	if(ignore[0] == 1, {
	this.funcOSC(\piano, direction);
	});
	if(ignore[1] == 1, {
	this.funcOSC(\drums, direction);
	});
	if(ignore[2] == 1, {
	this.funcOSC(\bass, direction);
	});
	}
	
	playScenes {arg startTime=0;
	var scenes, pinkLigth, step, step2;
	
scenes = [0,258.9, 400, 575,756, 875];
scenes = scenes - startTime; 

scenes = scenes.collect({|item| if(item.isNegative, {item = 0}, {item = item}) }); //with new start time

pinkLigth = (scenes - 7).reject({|item| item <= 0});

step = 0;
Routine({scenes.size.do({
scenes.differentiate[step].yield;
this.funcOSCAll((\scene ++ (step+1).asString), sceneLock);
step = step + 1;
});

}).play;

step2 = 0;
Routine({pinkLigth.size.do({
pinkLigth.differentiate[step2].yield;
this.funcOSCAll(\pink, sceneLock);
step2 = step2 + 1;
})}).play;

	}
	
	*initClass {
	
	SynthDef.writeOnce("playHeadphones", {arg bufnum=1, start=0, out=0, gates=1, amp=1, atk=0.1, dec=0.3;
	var signal, env;
	signal = PlayBuf.ar(1, bufnum, startPos: start*44100);
	env = EnvGen.kr(Env.asr(atk,1,dec), gates);
	Out.ar(out, (signal*env)*amp);
	});

	}

}