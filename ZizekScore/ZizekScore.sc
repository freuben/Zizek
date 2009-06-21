ZizekScore {var s, basicPath, <>score, <>osc, <>ipAdress, <>scoreType, <>network, <>mainFont, <>sceneFont, <>adjTimer, <>leftAdj, <>rightAdj, <>audio, <>buffer, <>startNumber=0;

	*new {arg scoreType = \piano, ipAdress = "localhost", audio = true, mainFont=5.7, sceneFont=3.7, adjTimer=0, leftAdj=2.2, rightAdj=7.1;
	^super.new.initZizekScore(scoreType, ipAdress, audio, mainFont, sceneFont, adjTimer, leftAdj, rightAdj);
	}
	
	initZizekScore {arg typeScore = \piano, ip = "localhost", boolAudio = true, fontMain = 5.7, fontScene = 3.7, timerAdj = 0, adjLeft=2.2, adjRight=7.1;
	var funcScore, width, red, green, yellow, blue, pink, black, string, stringScene, routinePno, routineBass, routineDrums, oscString, synth; 
	
	scoreType = typeScore;
	audio = boolAudio;
	mainFont = fontMain;
	sceneFont = fontScene;
	adjTimer = timerAdj;
	leftAdj = adjLeft;
	rightAdj = adjRight;
	ipAdress = ip;
	
	s = Server.default; //set s to default server
	basicPath = Document.current.path.dirname;

	if(audio, {
	case
	{scoreType == \piano} {buffer = Buffer.read(s, basicPath ++ "/piano.aif")}
	{scoreType == \drums} {buffer = Buffer.read(s, basicPath ++ "/drums.aif")}
	{scoreType == \bass} {buffer = Buffer.read(s, basicPath ++ "/bass.aif")}
	});
	
	case
	{scoreType == \piano} {
	score = AlgorithmicScore.screenBounds(string: "piano"); 
	oscString = '/piano';
	}
	{scoreType == \drums} {
	score = AlgorithmicScore.screenBounds(string: "drums"); 
	oscString = '/drums';
	}
	{scoreType == \bass} {
	score = AlgorithmicScore.screenBounds(string: "bass"); 
	oscString = '/bass';
	};
	
	//network
	network = NetAddr(ipAdress, 57120);
	//osc comunication
	osc = OSCresponder(network, oscString, { arg time, responder, msg;
	{funcScore.value(msg[1])}.defer;
	}).add;

	
	string = "   Like";
	stringScene = " Scene: 1";
	width = score.w.bounds.width;
	
	red = Color.new255(195, 43, 0);
	green = Color.new255(0, 100, 40);
	yellow = Color.new255(255, 150, 20);
	blue = Color.new255(0, 70, 160);
	pink = Color.new255(240, 109, 160);
	black = Color.black; 
	
	routinePno = Routine({var dif = 0, notes, colors;
	1.do({6.do({
	35.do({
	notes = Array.fill(6,{rrand(41,82)}).scramble;
	colors = Array.fill(6,\black);
	score.notes(notes, [0,1,2,3,4,5]*dif, colors);
	
	dif = dif + 0.3;
	rrand(0.07,0.01).yield;
	});
	30.do({
	notes = Array.fill(6,{rrand(38,83)}).scramble;
	colors = Array.fill(6,\black);
	score.notes(notes, [0,1,2,3,4,5]*dif, colors);
	
	dif = dif - 0.3;
	rrand(0.07,0.01).yield;
	});
	});
	dif = 0;
	
	5.do({
	notes = Array.fill(6,{rrand(41,82)}).scramble;
	colors = Array.fill(6,\black);
	score.notes(notes, [0,1,2,3,4,5]*dif, colors);
	
	dif = dif + 0.3;
	rrand(0.07,0.01).yield;
	});
	
	10.do({
	score.notes(notes, [0,1,2,3,4,5]*dif, colors);
	
	dif = dif + 0.3;
	rrand(0.07,0.01).yield;
	});
	
	50.do({
	notes = notes+2;
	score.notes(notes, [0,1,2,3,4,5]*dif, colors);
	rrand(0.07,0.1).yield;
	});
	
	30.do({
	notes = notes-4;
	score.notes(notes.midiMin(19), [0,1,2,3,4,5]*dif, colors);
	rrand(0.07,0.03).yield;
	});
	
	50.do({
	notes = notes-4;
	score.notes(notes.midiMin(19), [0,1,2,3,4,5]*dif, colors);
	rrand(0.07,0.01).yield;
	});
	
	
	100.do({
	notes = Array.fill(6,{rrand(41,82)}).scramble - 100;
	colors = Array.fill(6,\black);
	score.notes(notes.midiMin(19), [0,1,2,3,4,5]*dif, colors);
	
	dif = dif + 0.3;
	rrand(0.07,0.01).yield;
	});
	});
	});
	
	routineBass = Routine({
	var note1= 2, note2= -2, position=0, adjNote=0, randomN;
	100.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.001.yield;
	note1 = note1 + 0.1;
	note2 = note2 + 0.1;
	position = position + 0.02;
	});
	35.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.025.yield;
	note1 = note1 - 0.1;
	note2 = note2 + 0.1;
	position = position + 0.04;
	});
	"done".postln;
	30.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0001.yield;
	note1 = note1 + 0.1;
	note2 = note2 - 0.1;
	position = position + 0.04;
	});
	65.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.025.yield;
	note1 = note1 - 0.2;
	note2 = note2 - 0.1;
	position = position + 0.04;
	});
	70.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 + 0.125;
	note2 = note2 + 0.1;
	position = position + 0.04;
	});
	50.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.001.yield;
	note1 = note1 + 0.1;
	note2 = note2 + 0.1;
	position = position + 0.06;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 - 0.1;
	note2 = note2 - 0.1;
	position = position + 0.08;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 + 0.1;
	note2 = note2 + 0.1;
	position = position + 0.08;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 - 0.1;
	note2 = note2 - 0.1;
	position = position + 0.08;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 + 0.1;
	note2 = note2 + 0.1;
	position = position + 0.08;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 - 0.1;
	note2 = note2 - 0.1;
	position = position + 0.08;
	});
	20.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.0000001.yield;
	note1 = note1 + 0.1;
	note2 = note2 + 0.1;
	position = position + 0.08;
	});
	
	//new position
	position=0;
	note1 = note2-2;
	
	40.do({
	score.findNotes([[position, [0, 0, note1, 0, 0, 0]], [position, [0, 0, note2, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.001.yield;
	note1 = note1 - 0.2;
	note2 = note2 - 0.2;
	position = position + 0.01;
	});
	
	4.do({
	
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	6.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	7.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	8.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	9.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	10.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	
	});
	
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	9.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	8.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	7.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	6.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	});
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.1;
	adjNote = adjNote+0.01;
	position = position + 0.004;
	
	});
	
	});
	
	4.do({
	
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	6.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	7.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	8.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	9.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	10.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	
	});
	
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	9.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	8.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note1 = note1 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	7.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	6.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 + 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	});
	5.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); //midiNum, pos, color, staff, note1Type
	0.01.yield;
	note2 = note2 - 0.15;
	adjNote = adjNote-0.01;
	position = position + 0.02;
	
	});
	});
	
	150.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	note2 = note2 - 0.02;
	position = position + 0.008;
	});
	
	50.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.004;
	});
	
	50.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.002;
	});
	
	25.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.004;
	});
	
	25.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.008;
	});
	
	25.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.01;
	});
	
	10.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.1;
	});
	
	10.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.5;
	});
	
	
	10.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 1;
	});
	
	position=0;
	
	1.do({
	1.5.yield;
	30.do({
	score.findNotes([[position, [0, 0, note1-adjNote, 0, 0, 0]], [position, [0, 0, note2-adjNote, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 1;
	});
	});
	
	1.do({
	position=0;
	randomN = rrand(1,11); 
	1.5.yield;
	40.do({
	score.findNotes([[position, [0, 0, note1-adjNote+randomN, 0, 0, 0]], [position, [0, 0, note2-adjNote+randomN, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.7;
	});
	});
	
	1.do({
	position=0;
	randomN = rrand(1,11); 
	1.5.yield;
	40.do({
	score.findNotes([[position, [0, 0, note1-adjNote+randomN, 0, 0, 0]], [position, [0, 0, note2-adjNote+randomN, 0, 0, 0]]]); 
	0.001.yield;
	position = position + 0.95;
	});
	
	});
	
	});
	
	
	routineDrums = Routine({var func1, func2, func3, func4, num, rand1, slow;
	
	func1 = {arg dur; score.click1Note(dur) };
	func2 = {arg dur; score.click2Note(dur) };
	func3 = {arg dur; score.click3Note(dur) };
	func4 = {arg dur; score.click4Note(dur) };
	
	3.do({
	func1.value(0.1);
	(rrand(0.2,0.22)).yield;
	func4.value(0.1);
	(rrand(0.2,0.22)).yield;
	func2.value(0.1);
	(rrand(0.2,0.22)).yield;
	func3.value(0.1);
	(rrand(0.2,0.22)).yield;
	});
	
	9.do({
	func1.value(0.05);
	(rrand(0.2,0.22)/2).yield;
	func4.value(0.05);
	(rrand(0.2,0.22)/2).yield;
	func2.value(0.05);
	(rrand(0.2,0.22)/2).yield;
	func3.value(0.05);
	(rrand(0.2,0.22)/2).yield;
	});
	
	1.do({
	func1.value(0.03);
	(rrand(0.2,0.22)/3).yield;
	func4.value(0.03);
	(rrand(0.2,0.22)/3).yield;
	func2.value(0.03);
	(rrand(0.2,0.22)/3).yield;
	func3.value(0.03);
	(rrand(0.2,0.22)/3).yield;
	});
	
	2.do({
	func1.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	func4.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	func2.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	func3.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	});
	
	5.do({
	[func2, func3, func1, func4].choose.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	[func2, func3, func1, func4].choose.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	[func2, func3, func1, func4].choose.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	[func2, func3, func1, func4].choose.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	});
	
	40.do({
	func1.value(0.025);
	func2.value(0.025);
	func3.value(0.025);
	func4.value(0.025);
	(rrand(0.2,0.22)/4).yield;
	});
	
	40.do({
	[func2, func3, func1, func4].choose.value(0.05);
	[func2, func3, func1, func4].choose.value(0.05);
	(rrand(0.2,0.22)/4).yield;
	});
	
	8.do({
	[func2, func3].choose.value(0.05);
	num = 2;
	(rrand(0.2,0.22)/4).yield;
	num.do({
	
	rand1 = rrand(0.1,0.05);
	(rand1 + rand1).yield;
	slow = rrand(5,7.0)*[0.33,0.25].choose;
	[func1, func4].choose.value([rand1, slow/16].choose);
	});
	});
	
	1.do({
	func4.value(0.2);
	func3.value(0.2);
	func2.value(0.2);
	(rand1 + rand1*3).yield;
	});
	
	5.do({
	[func2, func3].choose.value(0.05);
	(rrand(0.2,0.22)/2).yield;
	2.do({
	
	rand1 = rrand(0.1,0.05);
	(rand1 + rand1).yield;
	slow = rrand(5,7.0)*[0.33,0.25].choose/2;
	[func1, func4].choose.value([rand1, slow/16].choose);
	});
	});
	
	5.do({
	slow = [rrand(5,7.0)*[1,0.5].choose, rrand(5,7.0)*[0.75,0.1875].choose].choose;
	[func2, func3].choose.value(slow/16);
	num = [rrand(2,5), 1].choose;
	(num/3/1.5).yield;
	
	num.do({
	rand1 = rrand(0.1,0.05);
	(rand1 + rand1*3).yield;
	slow = rrand(5,7.0)*[0.33,0.25].choose/1.5;
	[func1, func4].wchoose([1, 0.5].normalizeSum).value([rand1, slow/16].choose);
	});
	});
	
	1.do({
	slow = rrand(5,7.0)*[1,0.5].choose;
	func2.value(slow/16);
	num = rrand(3,6);
	(num/3/1.5).yield;
	num.do({
	
	rand1 = rrand(0.1,0.05);
	((rand1 + rand1*3)/1.5).yield;
	func1.value(rand1);
	});
	});
	
	2.do({
	slow = rrand(5,7.0)*[1,0.5].choose;
	func2.value(slow/16);
	num = rrand(3,6);
	(num/3).yield;
	num.do({
	
	rand1 = rrand(0.1,0.05);
	(rand1 + rand1*3).yield;
	func1.value(rand1);
	});
	});
	
	});

	//function:
	funcScore = {arg direction;
	var silence, free, like, with, unlike, without, solo;
	var scene1, scene2, scene3, scene4, scene5, end;
	silence = "Silence";
	free = "   Free";
	solo = "   Solo";
	like = "   Like";
	with = "   With";
	unlike = " Unlike";
	without = "Without";
	scene1 = " Scene: 1";
	scene2 = " Scene: 2";
	scene3 = " Scene: 3";
	scene4 = " Scene: 4";
	scene5 = " Scene: 5";
	end = "   END";
	
	case
	{direction.isNumber} {
	"Start Time: ".post;
	startNumber = direction;
	startNumber = startNumber.round(0.1).postln;
	}
	{direction == \audio} {
	synth = Synth("playHeadphonesScore", [\bufnum, buffer.bufnum.postln, \start, startNumber]);
	}
	{direction == \freeAudio} {
	synth.free;
	}
	{direction == \clear} {
	score.textClose;
	}
	{direction == \clear2} {
	score.text2Close;
	}
	{direction == \silence} {
	score.text(silence, "Helvetica", 140, width/mainFont, 240, 1, red);
	}
	{direction == \free} {
	score.text(free, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = free;
	}
	{direction == \solo} {
	score.text(solo, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = solo;
	}
	{direction == \like} {
	score.text(like, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = like;
	}
	{direction == \with} {
	score.text(with, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = with;
	}
	{direction == \unlike} {
	score.text(unlike, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = unlike;
	}
	{direction == \without} {
	score.text(without, "Helvetica", 140, width/mainFont, 240, 1, green);
	string = without;
	}
	{direction == \end} {
	score.text(end, "Helvetica", 140, width/mainFont, 240, 1, black);
	string = end;
	}
	{direction == \yellow} {
	score.text(string, "Helvetica", 140, width/mainFont, 240, 1, yellow);
	}
	{direction == \scene1} {
	score.text2(scene1, "Helvetica", 140/3, width/sceneFont, 400, 1, blue);
	stringScene = scene1;
	}
	{direction == \scene2} {
	score.text2(scene2, "Helvetica", 140/3, width/sceneFont, 400, 1, blue);
	stringScene = scene2;
	}
	{direction == \scene3} {
	score.text2(scene3, "Helvetica", 140/3, width/sceneFont, 400, 1, blue);
	stringScene = scene3;
	}
	{direction == \scene4} {
	score.text2(scene4, "Helvetica", 140/3, width/sceneFont, 400, 1, blue);
	stringScene = scene4;
	}
	{direction == \scene5} {
	score.text2(scene5, "Helvetica", 140/3, width/sceneFont, 400, 1, blue);
	stringScene = scene5;
	}
	{direction == \pink} {
	score.text2(stringScene, "Helvetica", (140/3), width/sceneFont, 400, 1, pink);	
	}
	{direction == \animation} {
	case
	{scoreType == \piano} {
	routinePno.reset;
	Routine({1.do({var time = 34;
	score.textClose;
	score.text2Close;
	score.score([\piano], 1.0, 1, 1.3); 
	routinePno.play( score.clock );
	score.timer(time, 0.85, 1, 30, rightWin:1.35+adjTimer);
	time.yield;
	routinePno.stop;
	score.clearWindow;
	});
	}).play( score.clock );
	}
	{scoreType == \drums} {
	Routine({1.do({var time=31;
	score.textClose;
	score.text2Close;
	score.click1(winAdd: 330, leftWin: leftAdj, border: false);
	score.click2(winAdd: 80, leftWin: rightAdj, border: false);
	score.click3(winAdd: 80, leftWin: leftAdj, border: false);
	score.click4(winAdd: 330, leftWin: rightAdj, border: false);
	routineDrums.reset;
	score.timer(time, 0.85, 1, 30, rightWin:1.35+adjTimer);
	routineDrums.play(score.clock);
	(time-0.1).yield;
	routineDrums.stop;
	0.1.yield;
	score.click1Close;
	score.click2Close;
	score.click3Close;
	score.click4Close;
	})}).play(score.clock)
	}
	{scoreType == \bass} {
	routineBass.reset;
	Routine({1.do({var time = 39.6;
	score.textClose;
	score.text2Close;
	score.score([\bass], 1.0, 1, 1.3); 
	routineBass.play( score.clock );
	score.timer(time, 0.85, 1, 30, rightWin:1.35+adjTimer);
	time.yield;
	routineBass.stop;
	score.clearWindow;
	});}).play( score.clock );
	
	}
	}
	};
		
	}
	
	
	*initClass {
	
	SynthDef.writeOnce("playHeadphonesScore", {arg bufnum=1, start=0, out=0, gates=1, amp=1, atk=0.1, dec=0.3;
	var signal, env;
	signal = PlayBuf.ar(1, bufnum, startPos: start*44100);
	env = EnvGen.kr(Env.asr(atk,1,dec), gates);
	Out.ar([out,out+1], (signal*env)*amp);
	});

	}

}